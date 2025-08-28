import {Navigate} from 'react-router-dom';
import {Outlet} from 'react-router';
import {hasAllowedRole, isAuthenticated} from "@/utils/authUtils.ts";

export interface RoleBasedRouteProps {
    allowedRoles: string[];
}

export default function RoleBasedRoute({allowedRoles}: RoleBasedRouteProps) {
    if (!isAuthenticated()) {
        return <Navigate to="/" replace/>;
    }

    if (!hasAllowedRole({allowedRoles})) {
        return <Navigate to="/" replace/>;
    }

    return <Outlet/>;
}