import {Navigate} from 'react-router-dom';
import {isAuthenticated} from "@/utils/authUtils.ts";
import {Outlet} from "react-router";

export default function ProtectedRoute() {

    if (!isAuthenticated()) {
        return <Navigate to="/login" replace/>;
    }

    return <Outlet/>;
};
