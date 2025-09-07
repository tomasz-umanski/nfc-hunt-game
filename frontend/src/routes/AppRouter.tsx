import {BrowserRouter, Routes, Route, Navigate} from 'react-router-dom';
import HomePage from "@/pages/HomePage.tsx";
import LoginPage from "@/pages/LoginPage.tsx";
import PublicRoute from "@/routes/PublicRoute.tsx";
import RegisterPage from "@/pages/RegisterPage.tsx";
import PrivacyPage from "@/pages/PrivacyPage.tsx";
import TermsPage from "@/pages/TermsPage.tsx";
import ProtectedRoute from "@/routes/ProtectedRoute.tsx";
import AccountSettingsPage from "@/pages/AccountSettingsPage.tsx";
import AdminPage from "@/pages/AdminPage.tsx";
import RoleBasedRoute from "@/routes/RoleBasedRoute.tsx";
import UserManagement from "@/pages/UserManagement.tsx";
import TagsManagement from "@/pages/TagsManagement.tsx";
import TagUnlockPage from "@/pages/TagUnlockPage.tsx";
import TagAccessDetailsPage from "@/pages/TagAccessDetailsPage.tsx";

export default function AppRouter() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<HomePage/>}/>
                <Route path="/terms" element={<TermsPage/>}/>
                <Route path="/privacy" element={<PrivacyPage/>}/>
                <Route path="/tag-access/:uuid" element={<TagAccessDetailsPage/>}/>
                <Route path="/unlock-tag-access/:uuid" element={<TagUnlockPage/>}/>

                <Route element={<PublicRoute/>}>
                    <Route path="/login" element={<LoginPage/>}/>
                    <Route path="/register" element={<RegisterPage/>}/>
                </Route>

                <Route element={<ProtectedRoute/>}>
                    <Route path="/account-settings" element={<AccountSettingsPage/>}/>
                </Route>

                <Route path="/admin" element={<RoleBasedRoute allowedRoles={['ADMIN']}/>}>
                    <Route element={<AdminPage/>}>
                        <Route index element={<Navigate to="/admin/users" replace/>}/>
                        <Route path="users" element={<UserManagement/>}/>
                        <Route path="tags" element={<TagsManagement/>}/>
                    </Route>
                </Route>

                <Route path="*" element={<Navigate to="/" replace/>}/>
            </Routes>
        </BrowserRouter>
    );
}
