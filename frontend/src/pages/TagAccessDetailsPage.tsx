import {useState, useEffect} from 'react';
import {useParams, useNavigate, useLocation} from 'react-router-dom';
import {useTranslation} from 'react-i18next';
import toast from 'react-hot-toast';
import {getByTagLocationId} from '@/api/tagAccess/tagAccess';
import {getImageUrl} from '@/api/upload/upload';
import {isAuthenticated} from '@/utils/authUtils';
import type {TagAccessResponseDto} from '@/types/tagAccess';
import HamburgerMenu from "@/components/layout/HamburgerMenu.tsx";

export default function TagAccessDetailsPage() {
    const {uuid} = useParams<{ uuid: string }>();
    const navigate = useNavigate();
    const location = useLocation();
    const {t} = useTranslation();

    const [tag, setTag] = useState<TagAccessResponseDto | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [animationStep, setAnimationStep] = useState(0);
    const [showAnimation, setShowAnimation] = useState(false);

    useEffect(() => {
        if (!isAuthenticated()) {
            const currentPath = location.pathname;
            navigate(`/login?redirect=${encodeURIComponent(currentPath)}`);
            return;
        }

        if (!uuid) {
            navigate('/');
            return;
        }

        fetchTagData();
    }, [uuid, navigate, location]);

    const fetchTagData = async () => {
        if (!uuid) return;

        try {
            setError(null);
            const data = await getByTagLocationId(uuid);
            setTag(data);

            if (data.unlocked && data.tagAccessResponseDetails?.unlockImages && data.tagAccessResponseDetails.unlockImages.length > 0) {
                setShowAnimation(true);
                setAnimationStep(0);
            }
        } catch (err: any) {
            const apiMessage = err?.response?.data?.error;
            console.error('Error fetching tag:', err);
            setError(apiMessage || t('tagAccessError'));
            toast.error(apiMessage || t('tagAccessError'));
        } finally {
        }
    };

    // Animation effect for unlock images
    useEffect(() => {
        if (!showAnimation || !tag?.tagAccessResponseDetails?.unlockImages) return;

        const sortedImages = [...tag.tagAccessResponseDetails.unlockImages]
            .sort((a, b) => a.displayOrder - b.displayOrder);

        if (animationStep < sortedImages.length) {
            const timeout = setTimeout(() => {
                setAnimationStep(prev => prev + 1);
            }, 1000);
            return () => clearTimeout(timeout);
        } else {
            // Animation finished, show final unlocked image immediately
            setShowAnimation(false);
        }
    }, [showAnimation, animationStep, tag]);

    const formatCoordinates = (lat: number, lng: number) => {
        return `${lat.toFixed(8)}°N, ${lng.toFixed(8)}°E`;
    };

    if (error || !tag) {
        return (
            <div className="min-h-screen flex flex-col items-center relative bg-primary-500">
                <HamburgerMenu/>
            </div>
        );
    }

    if (showAnimation && tag.tagAccessResponseDetails?.unlockImages) {
        const sortedImages = [...tag.tagAccessResponseDetails.unlockImages]
            .sort((a, b) => a.displayOrder - b.displayOrder);

        return (
            <div className="min-h-screen flex flex-col items-center relative bg-primary-500">
                <HamburgerMenu/>
                <div className="flex flex-col items-center justify-center text-center px-4 max-w-4xl w-full py-8">
                    <div className="bg-white rounded-lg shadow-md overflow-hidden max-w-lg w-full">
                        {animationStep < sortedImages.length && (
                            <div className="relative aspect-square overflow-hidden">
                                <img
                                    src={getImageUrl(sortedImages[animationStep].imageFilename)}
                                    alt={`${t('unlockImage')} ${animationStep + 1}`}
                                    className="w-full h-full object-cover animate-in fade-in zoom-in duration-500"
                                />
                            </div>
                        )}
                    </div>
                </div>
            </div>
        );
    }

    const isUnlocked = tag.unlocked;

    return (
        <div className="min-h-screen flex flex-col items-center relative bg-primary-500">
            <HamburgerMenu/>
            <div className="flex flex-col items-center justify-center text-center px-4 max-w-4xl w-full py-8">
                <div className="bg-white rounded-lg shadow-md overflow-hidden max-w-md w-full">

                    {/* Image Container */}
                    <div className="relative aspect-square overflow-hidden">
                        <img
                            src={isUnlocked && tag.tagAccessResponseDetails?.unlockedImageFilename
                                ? getImageUrl(tag.tagAccessResponseDetails.unlockedImageFilename)
                                : getImageUrl(tag.lockedImageFilename)}
                            alt={isUnlocked ? tag.tagAccessResponseDetails?.name || 'Unlocked location' : 'Locked location'}
                            className={`w-full h-full object-cover ${!isUnlocked ? 'blur-sm' : ''} hover:scale-105`}
                        />

                        {!isUnlocked && (
                            <div className="absolute inset-0 flex items-center justify-center bg-black bg-opacity-20">
                                <div className="text-center text-white bg-black bg-opacity-50 px-3 py-2 rounded-lg">
                                    <span className="font-mono text-sm">
                                        {formatCoordinates(tag.latitude, tag.longitude)}
                                    </span>
                                </div>
                            </div>
                        )}
                    </div>

                    {/* Content Section */}
                    {isUnlocked && tag.tagAccessResponseDetails && (
                        <div className="p-8 text-center">
                            <h3 className="text-xl font-semibold text-gray-800 mb-3">
                                {tag.tagAccessResponseDetails.name}
                            </h3>
                            <p className="text-gray-600 leading-relaxed">
                                {tag.tagAccessResponseDetails.description}
                            </p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}