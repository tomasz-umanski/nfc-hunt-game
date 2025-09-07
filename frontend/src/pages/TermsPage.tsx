import {useTranslation} from 'react-i18next';
import HamburgerMenu from "@/components/layout/HamburgerMenu.tsx";

export default function TermsPage() {
    const {t} = useTranslation();

    return (
        <div className="min-h-screen bg-primary-500">
            <HamburgerMenu/>

            <div className="container mx-auto px-4 py-8 max-w-4xl">
                {/* Header */}
                <div className="text-center mb-8">
                    <div className="flex items-center justify-center gap-3 mb-4">
                        <h1 className="text-3xl md:text-4xl font-bold text-gray-800">
                            {t('terms.title')}
                        </h1>
                    </div>
                </div>

                {/* Terms Content */}
                <div className="bg-primary-500 rounded-lg shadow-sm border border-gray-800 p-6 md:p-8 space-y-8">

                    {/* Section 1: General Provisions */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('terms.section1.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p><span className="font-medium">1.</span> {t('terms.section1.point1')}</p>
                            <p><span className="font-medium">2.</span> {t('terms.section1.point2')}</p>
                            <p><span className="font-medium">3.</span> {t('terms.section1.point3')}</p>
                            <p><span className="font-medium">4.</span> {t('terms.section1.point4')}</p>
                        </div>
                    </section>

                    {/* Section 2: Participants */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('terms.section2.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p><span className="font-medium">1.</span> {t('terms.section2.point1')}</p>
                            <p><span className="font-medium">2.</span> {t('terms.section2.point2')}</p>
                            <p><span className="font-medium">3.</span> {t('terms.section2.point3')}</p>
                        </div>
                    </section>

                    {/* Section 3: Game Rules */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('terms.section3.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p><span className="font-medium">1.</span> {t('terms.section3.point1')}</p>
                            <p><span className="font-medium">2.</span> {t('terms.section3.point2')}</p>
                            <p><span className="font-medium">3.</span> {t('terms.section3.point3')}</p>
                            <p><span className="font-medium">4.</span> {t('terms.section3.point4')}</p>
                            <p><span className="font-medium">5.</span> {t('terms.section3.point5')}</p>
                            <p><span className="font-medium">6.</span> {t('terms.section3.point6')}</p>
                        </div>
                    </section>

                    {/* Section 4: Technical Requirements */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('terms.section4.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p><span className="font-medium">1.</span> {t('terms.section4.point1')}</p>
                            <p><span className="font-medium">2.</span> {t('terms.section4.point2')}</p>
                            <p><span className="font-medium">3.</span> {t('terms.section4.point3')}</p>
                        </div>
                    </section>

                    {/* Section 5: Safety and Responsibility */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('terms.section5.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p><span className="font-medium">1.</span> {t('terms.section5.point1')}</p>
                            <p><span className="font-medium">2.</span> {t('terms.section5.point2')}</p>
                            <p><span className="font-medium">3.</span> {t('terms.section5.point3')}</p>
                            <p><span className="font-medium">4.</span> {t('terms.section5.point4')}</p>
                        </div>
                    </section>

                    {/* Section 6: Prizes */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('terms.section6.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p><span className="font-medium">1.</span> {t('terms.section6.point1')}</p>
                            <p><span className="font-medium">2.</span> {t('terms.section6.point2')}</p>
                            <p><span className="font-medium">3.</span> {t('terms.section6.point3')}</p>
                            <p><span className="font-medium">4.</span> {t('terms.section6.point4')}</p>
                            <p><span className="font-medium">5.</span> {t('terms.section6.point5')}</p>
                        </div>
                    </section>

                    {/* Section 7: Complaints */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('terms.section7.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p><span className="font-medium">1.</span> {t('terms.section7.point1')}</p>
                            <p><span className="font-medium">2.</span> {t('terms.section7.point2')}</p>
                        </div>
                    </section>

                    {/* Section 8: Personal Data */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('terms.section8.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p><span className="font-medium">1.</span> {t('terms.section8.point1')}</p>
                            <p><span className="font-medium">2.</span> {t('terms.section8.point2')}</p>
                            <p><span className="font-medium">3.</span> {t('terms.section8.point3')}</p>
                        </div>
                    </section>

                    {/* Section 9: Final Provisions */}
                    <section>
                        <h2 className="text-2xl font-semibold text-gray-900 mb-6 pb-2 border-b border-gray-800">
                            {t('terms.section9.title')}
                        </h2>
                        <div className="space-y-4 text-gray-700 leading-relaxed">
                            <p><span className="font-medium">1.</span> {t('terms.section9.point1')}</p>
                            <p><span className="font-medium">2.</span> {t('terms.section9.point2')}</p>
                            <p><span className="font-medium">3.</span> {t('terms.section9.point3')}</p>
                        </div>
                    </section>
                </div>

                {/* Footer */}
                <div className="text-center mt-12 pt-8 border-t border-gray-800">
                    <p className="text-gray-500 font-medium mb-2">{t('terms.effective_date')}</p>
                    <p className="text-gray-500">{t('terms.contact_info')}</p>
                </div>
            </div>
        </div>
    );
}