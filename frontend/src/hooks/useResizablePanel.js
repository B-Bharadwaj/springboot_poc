import { useCallback, useEffect, useLayoutEffect, useRef, useState } from 'react';

const STORAGE_KEY = 'apiPanelHeight';
const KEY_STEP = 20;

function maxHeight(minHeight) {
  return Math.max(Math.round(window.innerHeight * 0.8), minHeight);
}

function readSaved() {
  try {
    const saved = JSON.parse(localStorage.getItem(STORAGE_KEY));
    if (saved && typeof saved.height === 'number') return saved;
  } catch {
    // Storage blocked or bad JSON: fall back to the default.
  }
  return null;
}

function save(height, collapsed) {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify({ height, collapsed }));
  } catch {
    // Storage blocked: the height just isn't remembered.
  }
}

// Drag, keyboard and double-click resizing for a panel pinned to the bottom.
export default function useResizablePanel({ minHeight = 36, defaultHeight = 220 } = {}) {
  const panelRef = useRef(null);
  const handleRef = useRef(null);
  const initial = useRef(null);
  if (initial.current === null) {
    const saved = readSaved();
    const last = saved && saved.height > minHeight ? saved.height : defaultHeight;
    initial.current = { last, collapsed: !!(saved && saved.collapsed) };
  }
  // Last expanded height, restored by the toggle.
  const lastHeight = useRef(initial.current.last);
  const heightRef = useRef(initial.current.collapsed ? minHeight : initial.current.last);
  const drag = useRef(null);
  const frame = useRef(0);
  const [collapsed, setCollapsed] = useState(initial.current.collapsed);

  const clamp = useCallback(
    (h) => Math.round(Math.min(Math.max(h, minHeight), maxHeight(minHeight))),
    [minHeight]
  );

  // Writes the height to the DOM directly, without a React render.
  const apply = useCallback(
    (h) => {
      heightRef.current = h;
      if (panelRef.current) panelRef.current.style.height = `${h}px`;
      document.documentElement.style.setProperty('--panel-height', `${h}px`);
      if (handleRef.current) {
        handleRef.current.setAttribute('aria-valuenow', String(h));
        handleRef.current.setAttribute('aria-valuemax', String(maxHeight(minHeight)));
      }
    },
    [minHeight]
  );

  const commit = useCallback(
    (h) => {
      cancelAnimationFrame(frame.current);
      frame.current = 0;
      apply(h);
      const isCollapsed = h <= minHeight;
      if (!isCollapsed) lastHeight.current = h;
      setCollapsed(isCollapsed);
      save(lastHeight.current, isCollapsed);
    },
    [apply, minHeight]
  );

  const toggle = useCallback(() => {
    commit(heightRef.current > minHeight ? minHeight : clamp(lastHeight.current));
  }, [clamp, commit, minHeight]);

  useLayoutEffect(() => {
    apply(clamp(heightRef.current));
  }, [apply, clamp]);

  useEffect(() => {
    const onResize = () => apply(clamp(heightRef.current));
    window.addEventListener('resize', onResize);
    return () => {
      window.removeEventListener('resize', onResize);
      cancelAnimationFrame(frame.current);
    };
  }, [apply, clamp]);

  function onPointerDown(e) {
    if (e.button !== 0) return;
    e.currentTarget.setPointerCapture(e.pointerId);
    drag.current = {
      startY: e.clientY,
      startHeight: heightRef.current,
      height: heightRef.current,
      userSelect: document.body.style.userSelect,
    };
    document.body.style.userSelect = 'none';
  }

  function onPointerMove(e) {
    if (!drag.current) return;
    // Moving up (smaller clientY) makes the panel taller.
    drag.current.height = clamp(drag.current.startHeight + drag.current.startY - e.clientY);
    if (!frame.current) {
      frame.current = requestAnimationFrame(() => {
        frame.current = 0;
        if (drag.current) apply(drag.current.height);
      });
    }
  }

  function endDrag(e) {
    if (!drag.current) return;
    const { height, userSelect } = drag.current;
    drag.current = null;
    if (e.currentTarget.hasPointerCapture(e.pointerId)) e.currentTarget.releasePointerCapture(e.pointerId);
    document.body.style.userSelect = userSelect;
    commit(height);
  }

  function onKeyDown(e) {
    const h = heightRef.current;
    const next = {
      ArrowUp: h + KEY_STEP,
      ArrowDown: h - KEY_STEP,
      Home: minHeight,
      End: maxHeight(minHeight),
    }[e.key];
    if (next === undefined) return;
    e.preventDefault();
    commit(clamp(next));
  }

  const handleProps = {
    ref: handleRef,
    role: 'separator',
    tabIndex: 0,
    'aria-orientation': 'horizontal',
    'aria-valuemin': minHeight,
    'aria-valuemax': maxHeight(minHeight),
    'aria-valuenow': heightRef.current,
    onPointerDown,
    onPointerMove,
    onPointerUp: endDrag,
    onPointerCancel: endDrag,
    onDoubleClick: toggle,
    onKeyDown,
  };

  return { panelRef, handleProps, collapsed, toggle };
}
