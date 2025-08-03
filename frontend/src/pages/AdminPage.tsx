import {useTranslation} from 'react-i18next';
import HamburgerMenu from "@/components/layout/HamburgerMenu.tsx";

export default function AdminPage() {
    const {t} = useTranslation();

    return (
        <div className="min-h-screen flex flex-col items-center justify-center relative bg-gray-100">
            <HamburgerMenu/>
            <h1 className="text-3xl font-bold">{t('welcome')}</h1>
        </div>
    );
}
