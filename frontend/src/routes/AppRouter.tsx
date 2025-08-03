import {BrowserRouter, Routes, Route, Navigate} from 'react-router-dom';
import HomePage from "@/pages/HomePage.tsx";
import LoginPage from "@/pages/LoginPage.tsx";
import PublicRoute from "@/routes/PublicRoute.tsx";

export default function AppRouter() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<HomePage/>}/>

                <Route element={<PublicRoute/>}>
                    <Route path="/login" element={<LoginPage/>}/>
                </Route>

                <Route path="*" element={<Navigate to="/" replace/>}/>
            </Routes>
        </BrowserRouter>
    );
}
