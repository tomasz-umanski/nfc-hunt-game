import {useState, useEffect, useRef} from 'react';
import {useNavigate} from 'react-router-dom';
import type {TagAccessResponseDto} from '@/types/tagAccess';
import {getAllTagsPublic, getAllTagsWithAccessRequest} from "@/api/tagAccess/tagAccess.ts";
import {getImageUrl} from "@/api/upload/upload.ts";
import {isAuthenticated} from "@/utils/authUtils.ts";
import toast from "react-hot-toast";
import {t} from "i18next";

export default function TagAccessCards() {
    const [tags, setTags] = useState<TagAccessResponseDto[]>([]);
    const hasFetched = useRef(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchTags = async () => {
            if (hasFetched.current) return;
            hasFetched.current = true;

            try {
                let data;
                if (isAuthenticated()) {
                    data = await getAllTagsWithAccessRequest();
                } else {
                    data = await getAllTagsPublic();
                }
                setTags(data);
            } catch (err: any) {
                const apiMessage = err?.response?.data?.error;
                console.error('Error fetching tags:', err);
                toast.error(apiMessage || t('tagAccessError'));
            } finally {
            }
        };

        fetchTags();
    }, []);

    const formatCoordinates = (lat: number, lng: number) => {
        return `${lat.toFixed(8)}°N, ${lng.toFixed(8)}°E`;
    };

    const handleTagClick = (tagLocationId: string) => {
        navigate(`/tag-access/${tagLocationId}`);
    };

    return (
        <div className="w-full max-w-4xl px-4">
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6">
                {tags.map((tag, index) => {
                    const isUnlocked = isAuthenticated() && tag.unlocked;

                    return (
                        <div
                            key={index}
                            className="bg-white rounded-lg shadow-md overflow-hidden cursor-pointer"
                            onClick={() => handleTagClick(tag.tagLocationId)}
                        >
                            {/* Image Container */}
                            <div className="relative aspect-square overflow-hidden">
                                <img
                                    src={isUnlocked && tag.tagAccessResponseDetails?.unlockedImageFilename
                                        ? getImageUrl(tag.tagAccessResponseDetails.unlockedImageFilename)
                                        : getImageUrl(tag.lockedImageFilename)}
                                    alt={isUnlocked ? tag.tagAccessResponseDetails?.name || 'Unlocked location' : 'Locked location'}
                                    className={`w-full h-full object-cover ${!isUnlocked ? 'blur-md' : ''}`}
                                />

                                {!isUnlocked && (
                                    <div
                                        className="absolute inset-0 flex items-center justify-center bg-black bg-opacity-20">
                                        <div
                                            className="text-center text-white bg-black bg-opacity-50 px-3 py-2 rounded-lg">
                                            <span className="font-mono text-sm">
                                                {formatCoordinates(tag.latitude, tag.longitude)}
                                            </span>
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
};