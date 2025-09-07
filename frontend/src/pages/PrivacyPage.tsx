import {useTranslation} from 'react-i18next';
import HamburgerMenu from "@/components/layout/HamburgerMenu.tsx";

export default function PrivacyPage() {
    const {t} = useTranslation();

    return (
        <div className="min-h-screen bg-primary-500">
            <HamburgerMenu/>

            <div className="container mx-auto px-4 py-8 max-w-4xl">
                {/* Header */}
                <div className="text-center mb-8">
                    <div className="flex items-center justify-center gap-3 mb-4">
                        <h1 className="text-3xl md:text-4xl font-bold text-gray-800">
                            {t('privacy.title')}
                        </h1>
                    </div>
                </div>

                {/* Privacy Content */}
                <div className="bg-primary-500 rounded-lg shadow-sm border border-gray-800 p-6 md:p-8 space-y-8">

                    {/* Section 1: General Information */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('privacy.section1.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p><span className="font-medium">1.</span> {t('privacy.section1.point1')}</p>
                            <p><span className="font-medium">2.</span> {t('privacy.section1.point2')}</p>
                        </div>
                    </section>

                    {/* Section 2: Scope of Data Processing */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('privacy.section2.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p>{t('privacy.section2.intro')}</p>
                            <ul className="list-disc list-inside space-y-2 ml-4">
                                <li>{t('privacy.section2.data1')}</li>
                                <li>{t('privacy.section2.data2')}</li>
                                <li>{t('privacy.section2.data3')}</li>
                            </ul>
                        </div>
                    </section>

                    {/* Section 3: Purpose and Legal Basis */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('privacy.section3.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p><span className="font-medium">1.</span> {t('privacy.section3.point1.intro')}</p>
                            <ul className="list-disc list-inside space-y-2 ml-8">
                                <li>{t('privacy.section3.point1.purpose1')}</li>
                                <li>{t('privacy.section3.point1.purpose2')}</li>
                                <li>{t('privacy.section3.point1.purpose3')}</li>
                                <li>{t('privacy.section3.point1.purpose4')}</li>
                            </ul>
                            <p><span className="font-medium">2.</span> {t('privacy.section3.point2.intro')}</p>
                            <ul className="list-disc list-inside space-y-2 ml-8">
                                <li>{t('privacy.section3.point2.basis1')}</li>
                                <li>{t('privacy.section3.point2.basis2')}</li>
                            </ul>
                        </div>
                    </section>

                    {/* Section 4: Data Retention */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('privacy.section4.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p><span className="font-medium">1.</span> {t('privacy.section4.point1')}</p>
                            <p><span className="font-medium">2.</span> {t('privacy.section4.point2')}</p>
                        </div>
                    </section>

                    {/* Section 5: Data Sharing */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('privacy.section5.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p><span className="font-medium">1.</span> {t('privacy.section5.point1')}</p>
                            <p><span className="font-medium">2.</span> {t('privacy.section5.point2')}</p>
                        </div>
                    </section>

                    {/* Section 6: User Rights */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('privacy.section6.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p>{t('privacy.section6.intro')}</p>
                            <ul className="list-disc list-inside space-y-2 ml-4">
                                <li>{t('privacy.section6.right1')}</li>
                                <li>{t('privacy.section6.right2')}</li>
                                <li>{t('privacy.section6.right3')}</li>
                                <li>{t('privacy.section6.right4')}</li>
                                <li>{t('privacy.section6.right5')}</li>
                                <li>{t('privacy.section6.right6')}</li>
                            </ul>
                        </div>
                    </section>

                    {/* Section 7: Voluntary Data Provision */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('privacy.section7.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p>{t('privacy.section7.content')}</p>
                        </div>
                    </section>

                    {/* Section 8: Contact */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('privacy.section8.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p>{t('privacy.section8.content')}</p>
                        </div>
                    </section>
                </div>

                {/* Footer */}
                <div className="text-center mt-12 pt-8 border-t border-gray-800">
                    <p className="text-gray-500 font-medium mb-2">{t('privacy.effective_date')}</p>
                    <p className="text-gray-500">{t('privacy.contact_info')}</p>
                </div>
            </div>
        </div>
    );
}