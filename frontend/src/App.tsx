import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import DashboardPage from "./pages/DashboardPage";
import PortfolioPage from "./pages/PortfolioPage";
import RegistrationPage from "./pages/RegistrationPage";
import AlertsPage from "./pages/AlertsPage";
import SlaReportsPage from "./pages/SlaReportsPage";
import AdminPage from "./pages/AdminPage";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/portfolio" element={<PortfolioPage />} />
        <Route path="/register" element={<RegistrationPage />} />
        <Route path="/alerts" element={<AlertsPage />} />
        <Route path="/reports" element={<SlaReportsPage />} />
        <Route path="/admin" element={<AdminPage />} />
      </Routes>
    </BrowserRouter>
  );
}
