import i18n from 'i18next';
import {initReactI18next} from 'react-i18next';
import en from '@/i18n/locales/en/translation.json';
import pl from '@/i18n/locales/pl/translation.json';

const detectBrowserLang = (): string => {
    const browserLang = navigator.language || navigator.languages?.[0] || 'en';

    if (browserLang.toLowerCase().startsWith('pl')) return 'pl';
    return 'en';
};

const storedLang = localStorage.getItem('lang');
const initialLang = storedLang || detectBrowserLang();

i18n
    .use(initReactI18next)
    .init({
        resources: {
            en: {translation: en},
            pl: {translation: pl},
        },
        lng: initialLang,
        fallbackLng: 'en',
        interpolation: {escapeValue: false},
    });
