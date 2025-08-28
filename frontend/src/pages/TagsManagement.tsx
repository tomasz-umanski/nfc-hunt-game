import {useTranslation} from 'react-i18next';

export default function TagsManagement() {
    const {t} = useTranslation();

    return (
        <div>
            <h2 className="text-lg font-semibold mb-2">{t('location_management')}</h2>
            <p>{t('location_management_welcome')}</p>
        </div>
    );
}