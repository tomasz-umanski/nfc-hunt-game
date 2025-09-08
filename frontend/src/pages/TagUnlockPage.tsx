import {useState, useEffect} from 'react';
import {useParams, useNavigate, useLocation} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import toast from 'react-hot-toast';
import {MapPin, Loader, Lock, Unlock} from 'lucide-react';
import {unlockTagRequest} from '@/api/tagAccess/tagAccess';
import {isAuthenticated} from '@/utils/authUtils';
import type {UnlockTagRequestDto} from '@/types/tagAccess';
import HamburgerMenu from "@/components/layout/HamburgerMenu.tsx";

export default function TagUnlockPage() {
    const {uuid} = useParams<{ uuid: string }>();
    const navigate = useNavigate();
    const location = useLocation();
    const {t} = useTranslation();

    const [step, setStep] = useState<'auth' | 'location' | 'unlocking' | 'success' | 'animation'>('auth');
    const [locationError, setLocationError] = useState<string | null>(null);
    const [isProcessing, setIsProcessing] = useState(false);

    useEffect(() => {
        if (!uuid) {
            navigate('/');
            return;
        }

        if (isAuthenticated()) {
            setStep('location');
        } else {
            const currentPath = location.pathname;
            navigate(`/login?redirect=${encodeURIComponent(currentPath)}`);
        }
    }, [uuid, navigate, location]);

    const requestLocationAndUnlock = async () => {
        if (!uuid) return;

        setIsProcessing(true);
        setLocationError(null);

        if (!navigator.geolocation) {
            setLocationError(t('tagUnlock.locationNotSupported'));
            setIsProcessing(false);
            return;
        }

        const options = {
            enableHighAccuracy: true,
            timeout: 10000,
            maximumAge: 300000 // 5 minutes
        };

        navigator.geolocation.getCurrentPosition(
            async (position) => {
                const roundedLatitude = Math.round(position.coords.latitude * 100000000) / 100000000;
                const roundedLongitude = Math.round(position.coords.longitude * 100000000) / 100000000;

                const locationData = {
                    latitude: roundedLatitude,
                    longitude: roundedLongitude
                };

                setLocationError(null);

                setStep('unlocking');

                try {
                    const unlockData: UnlockTagRequestDto = {
                        nfcTagUuid: uuid,
                        latitude: locationData.latitude,
                        longitude: locationData.longitude
                    };

                    const result = await unlockTagRequest(unlockData);
                    navigate(`/tag-access/${result.tagLocationId}`);
                } catch (error: any) {
                    const apiMessage = error?.response?.data?.error;
                    toast.error(apiMessage || t('tagUnlock.unlockFailed'));
                    setStep('location');
                } finally {
                    setIsProcessing(false);
                }
            },
            (error) => {
                let errorMessage = t('tagUnlock.locationError');

                switch (error.code) {
                    case error.PERMISSION_DENIED:
                        errorMessage = t('tagUnlock.locationDenied');
                        break;
                    case error.POSITION_UNAVAILABLE:
                        errorMessage = t('tagUnlock.locationUnavailable');
                        break;
                    case error.TIMEOUT:
                        errorMessage = t('tagUnlock.locationTimeout');
                        break;
                }

                setLocationError(errorMessage);
                setIsProcessing(false);
            },
            options
        );
    };

    const renderLocationStep = () => (
        <div className="min-h-screen flex flex-col items-center relative bg-primary-500">
            <HamburgerMenu/>

            <div className="flex flex-col items-center justify-center text-center px-4 max-w-4xl w-full py-8">
                <div className="bg-white rounded-lg shadow-md overflow-hidden max-w-md w-full">
                    <div className="p-8 text-center">
                        <MapPin className="mx-auto h-16 w-16 text-primary-600 mb-4"/>
                        <h1 className="text-2xl font-bold text-gray-900 mb-6">
                            {t('tagUnlock.title')}
                        </h1>

                        {locationError && (
                            <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg">
                                <p className="text-red-700 text-sm">{locationError}</p>
                            </div>
                        )}

                        <button
                            onClick={requestLocationAndUnlock}
                            disabled={isProcessing}
                            className="w-full bg-primary-600 hover:bg-primary-700 disabled:bg-primary-300 text-white font-semibold py-3 px-4 rounded-lg transition-colors flex items-center justify-center"
                        >
                            <>
                                <Unlock className="h-5 w-5 mr-2"/>
                                {t('tagUnlock.unlockTag')}
                            </>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );

    const renderUnlockingStep = () => (
        <div className="min-h-screen flex flex-col items-center relative bg-primary-500">
            <HamburgerMenu/>

            <div className="flex flex-col items-center justify-center text-center px-4 max-w-4xl w-full py-8">
                <div className="bg-white rounded-lg shadow-md overflow-hidden max-w-md w-full">
                    <div className="p-8 text-center">
                        <Loader className="animate-spin h-16 w-16 text-primary-600 mx-auto mb-4"/>
                        <h2 className="text-2xl font-bold text-gray-900 mb-2">
                            {t('tagUnlock.unlocking')}
                        </h2>
                        <p className="text-gray-600">
                            {t('tagUnlock.unlockingDescription')}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );

    switch (step) {
        case 'location':
            return renderLocationStep();
        case 'unlocking':
            return renderUnlockingStep();
        default:
            return (
                <div className="min-h-screen flex flex-col items-center relative bg-primary-500">
                    <HamburgerMenu/>

                    <div className="flex flex-col items-center justify-center text-center px-4 max-w-4xl w-full py-8">
                        <div className="bg-white rounded-lg shadow-md overflow-hidden max-w-md w-full">
                            <div className="p-8 text-center">
                                <Lock className="h-16 w-16 text-gray-400 mx-auto mb-4"/>
                                <h2 className="text-2xl font-bold text-gray-900 mb-2">
                                    {t('tagUnlock.authRequired')}
                                </h2>
                                <p className="text-gray-600">
                                    {t('tagUnlock.authRequiredDescription')}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            );
    }
}