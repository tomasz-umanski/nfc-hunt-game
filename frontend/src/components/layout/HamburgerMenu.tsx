import {useEffect, useRef, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {Link, useNavigate} from 'react-router-dom';
import {Globe} from 'lucide-react';
import {useAuthStore} from '@/store/authStore';
import {hasAdminRole, isAuthenticated} from '@/utils/authUtils';
import {logoutRequest} from "@/api/auth/auth.ts";
import toast from "react-hot-toast";

export default function HamburgerMenu() {
    const {i18n, t} = useTranslation();
    const [open, setOpen] = useState(false);
    const menuRef = useRef<HTMLDivElement>(null);
    const navigate = useNavigate();
    const logout = useAuthStore((state) => state.logout);
    const refreshToken = useAuthStore((state) => state.refreshToken);

    const toggleLang = () => {
        const newLang = i18n.language === 'en' ? 'pl' : 'en';
        i18n.changeLanguage(newLang);
        localStorage.setItem('lang', newLang);
    };

    const handleLogout = async () => {
        if (!refreshToken) {
            logout();
            setOpen(false);
            navigate('/logout');
            return;
        }

        try {
            await toast.promise(
                logoutRequest({refreshToken}),
                {
                    loading: t('logout_loading'),
                    success: t('logout_success'),
                    error: (err: any) => err?.response?.data?.error || t('logout_error'),
                }
            );
        } catch (err: any) {
            if (err?.response?.status === 401) {
                logout();
                setOpen(false);
                navigate('/logout');
                return;
            }
        }

        logout();
        setOpen(false);
        navigate('/logout');
    };

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
                setOpen(false);
            }
        };

        if (open) {
            document.addEventListener('mousedown', handleClickOutside);
        } else {
            document.removeEventListener('mousedown', handleClickOutside);
        }

        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, [open]);

    return (
        <div className="fixed top-4 right-4 z-50">
            <div className="relative" ref={menuRef}>
                <button
                    className="w-10 h-10 bg-white text-black rounded-full flex items-center justify-center"
                    onClick={() => setOpen(!open)}
                    aria-label="Menu"
                >
                    ☰
                </button>

                {open && (
                    <div
                        className="absolute right-0 mt-2 p-4 bg-white shadow-lg rounded-lg flex flex-col gap-3 min-w-[180px]">
                        <button
                            onClick={toggleLang}
                            className="flex items-center gap-2 text-sm hover:underline text-left"
                        >
                            <Globe size={16}/>
                            {i18n.language.toUpperCase()}
                        </button>
                        <Link
                            to="/"
                            className="text-sm hover:underline text-left"
                            onClick={() => setOpen(false)}
                        >
                            {t('home')}
                        </Link>

                        <Link
                            to="/terms"
                            className="text-sm hover:underline text-left"
                            onClick={() => setOpen(false)}
                        >
                            {t('terms.title')}
                        </Link>

                        <Link
                            to="/privacy"
                            className="text-sm hover:underline text-left"
                            onClick={() => setOpen(false)}
                        >
                            {t('privacy.title')}
                        </Link>

                        {hasAdminRole() && (
                            <Link
                                to="/admin"
                                className="text-sm hover:underline text-left"
                                onClick={() => setOpen(false)}
                            >
                                {t('admin_panel')}
                            </Link>
                        )}
                        {isAuthenticated() ? (
                            <div className="flex flex-col gap-3">
                                <Link
                                    to="/account-settings"
                                    className="text-sm hover:underline text-left"
                                    onClick={() => setOpen(false)}
                                >
                                    {t('account_settings')}
                                </Link>
                                <button
                                    onClick={handleLogout}
                                    className="text-sm hover:underline text-left"
                                >
                                    {t('logout')}
                                </button>
                            </div>
                        ) : (
                            <div className="flex flex-col gap-3">
                                <Link
                                    to="/login"
                                    className="text-sm hover:underline text-left"
                                    onClick={() => setOpen(false)}
                                >
                                    {t('login')}
                                </Link>
                                <Link
                                    to="/register"
                                    className="text-sm hover:underline text-left"
                                    onClick={() => setOpen(false)}
                                >
                                    {t('register')}
                                </Link>
                            </div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
}
