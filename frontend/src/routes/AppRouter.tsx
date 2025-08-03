import {BrowserRouter, Routes, Route, Navigate} from 'react-router-dom';
import HomePage from "@/pages/HomePage.tsx";
import LoginPage from "@/pages/LoginPage.tsx";
import PublicRoute from "@/routes/PublicRoute.tsx";
import RegisterPage from "@/pages/RegisterPage.tsx";
import PrivacyPage from "@/pages/PrivacyPage.tsx";
import TermsPage from "@/pages/TermsPage.tsx";

export default function AppRouter() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<HomePage/>}/>
                <Route path="/terms" element={<TermsPage/>}/>
                <Route path="/privacy" element={<PrivacyPage/>}/>

                <Route element={<PublicRoute/>}>
                    <Route path="/login" element={<LoginPage/>}/>
                    <Route path="/register" element={<RegisterPage/>}/>
                </Route>

                <Route path="*" element={<Navigate to="/" replace/>}/>
            </Routes>
        </BrowserRouter>
    );
}
