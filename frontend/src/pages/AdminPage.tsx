import {NavLink, Outlet} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import {Users, Tag} from 'lucide-react';
import HamburgerMenu from '@/components/layout/HamburgerMenu';

export default function AdminPage() {
    const {t} = useTranslation();

    const links = [
        {
            to: 'users',
            label: t('user_management'),
            icon: <Users size={16} className="mr-2"/>,
        },
        {
            to: 'tags',
            label: t('tags_management'),
            icon: <Tag size={16} className="mr-2"/>,
        },
    ];

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <HamburgerMenu/>

            <header className="p-4 border-b border-gray-200">
                <h1 className="text-xl md:text-2xl font-semibold text-gray-800">
                    {t('admin_panel')}
                </h1>
            </header>

            <nav className="flex overflow-x-auto bg-white border-b border-gray-200">
                {links.map((link) => (
                    <NavLink
                        key={link.to}
                        to={link.to}
                        className={({isActive}) =>
                            `flex items-center whitespace-nowrap px-4 py-3 text-sm font-medium border-b-2 transition-colors ${
                                isActive
                                    ? 'text-blue-600 border-blue-600 bg-blue-50'
                                    : 'text-gray-500 border-transparent hover:text-blue-500 hover:bg-gray-100'
                            }`
                        }
                    >
                        {link.icon}
                        {link.label}
                    </NavLink>
                ))}
            </nav>

            <main className="p-4 flex-1 bg-gray-50 overflow-auto">
                <Outlet/>
            </main>
        </div>
    );
}