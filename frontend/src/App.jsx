import { Navigate, Route, Routes } from 'react-router-dom';
import ProtectedRoute from './components/ProtectedRoute.jsx';
import ActivityPanel from './components/ActivityPanel.jsx';
import PublicLayout from './components/PublicLayout.jsx';
import Overview from './pages/Overview.jsx';
import LoginPage from './pages/LoginPage.jsx';
import ProductsPage from './pages/ProductsPage.jsx';
import OrderPage from './pages/OrderPage.jsx';
import OrdersPage from './pages/OrdersPage.jsx';
import DemoPage from './pages/DemoPage.jsx';
import NotFoundPage from './pages/NotFoundPage.jsx';

export default function App() {
  return (
    <div className="app">
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route element={<PublicLayout />}>
          <Route path="/overview" element={<Overview />} />
        </Route>
        <Route element={<ProtectedRoute />}>
          <Route path="/" element={<Navigate to="/products" replace />} />
          <Route path="/products" element={<ProductsPage />} />
          <Route path="/order" element={<OrderPage />} />
          <Route path="/orders" element={<OrdersPage />} />
          <Route path="/demo" element={<DemoPage />} />
        </Route>
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
      <ActivityPanel />
    </div>
  );
}
