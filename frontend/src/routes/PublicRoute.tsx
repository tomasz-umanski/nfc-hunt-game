import {Navigate} from 'react-router-dom';
import {isAuthenticated} from "@/utils/authUtils.ts";
import {Outlet} from "react-router";

export default function PublicRoute() {

    if (isAuthenticated()) {
        return <Navigate to="/" replace/>;
    }

    return <Outlet/>;
}
