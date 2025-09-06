import {useEffect, useState} from 'react';
import {useForm} from 'react-hook-form';
import {zodResolver} from '@hookform/resolvers/zod';
import * as z from 'zod';
import {useTranslation} from 'react-i18next';
import {
    Plus,
    Trash2,
    Save,
    X,
    MapPin,
    Image,
    ChevronUp,
    ChevronDown,
    ExternalLink,
    Nfc,
    Edit
} from 'lucide-react';
import {v4 as uuidv4} from 'uuid';

import {
    getAllTagLocationsRequest,
    createTagLocationRequest,
    updateTagLocationRequest,
    deleteTagLocationRequest
} from '@/api/tagLocation/tagLocation';
import 'leaflet/dist/leaflet.css';

import {MapContainer, TileLayer, Marker, useMapEvents} from "react-leaflet";
import L from "leaflet";

// Custom marker icon fix for leaflet
new L.Icon({
    iconUrl: "https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png",
    iconRetinaUrl: "https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon-2x.png",
    shadowUrl: "https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png",
    iconSize: [25, 41],
    iconAnchor: [12, 41],
});

// Component for handling map clicks
function LocationPicker({setValue}: { setValue: any }) {
    useMapEvents({
        click(e) {
            const lat = Number(e.latlng.lat.toFixed(8));
            const lng = Number(e.latlng.lng.toFixed(8));

            setValue("latitude", lat, {shouldValidate: true});
            setValue("longitude", lng, {shouldValidate: true});
        },
    });
    return null;
}

import type {
    TagLocationResponseDto,
    CreateTagLocationDto,
    UpdateTagLocationDto,
    CreateTagLocationUnlockImageDto
} from '@/types/tagLocation';
import ImageUpload from "@/components/layout/ImageUpload.tsx";
import toast from "react-hot-toast";

// Zod schema for tag validation
const unlockImageSchema = z.object({
    imageFilename: z.string().min(1, 'validation.image_filename_required'),
    displayOrder: z.number().min(1, 'validation.display_order_min').max(50, 'validation.display_order_max')
});

const tagSchema = z.object({
    name: z.string()
        .min(1, 'validation.tag_name_required')
        .max(100, 'validation.tag_name_max'),
    description: z.string()
        .max(1000, 'validation.description_max')
        .optional(),
    nfcTagUuid: z.string()
        .min(1, 'validation.nfc_uuid_required')
        .max(50, 'validation.nfc_uuid_max'),
    latitude: z.number('validation.latitude_required')
        .min(-90, 'validation.latitude_range')
        .max(90, 'validation.latitude_range'),
    longitude: z.number('validation.longitude_required')
        .min(-180, 'validation.longitude_range')
        .max(180, 'validation.longitude_range'),
    lockedImageFilename: z.string().min(1, 'validation.image_filename_required'),
    unlockedImageFilename: z.string().min(1, 'validation.image_filename_required'),
    unlockImages: z.array(unlockImageSchema).min(1, 'validation.unlock_images_min').default([])
});

type TagFormData = z.infer<typeof tagSchema>;

interface EditableTag extends TagLocationResponseDto {
    isEditing?: boolean;
}

const TagsManagement = () => {
    const {t} = useTranslation();

    const [tags, setTags] = useState<EditableTag[]>([]);
    const [isCreating, setIsCreating] = useState(false);
    const [editingTag, setEditingTag] = useState<string | null>(null);
    const [expandedTag, setExpandedTag] = useState<string | null>(null);
    const [mapCenter, setMapCenter] = useState<[number, number]>([54.3520, 18.6466]);

    const {
        register,
        handleSubmit,
        formState: {errors, isSubmitting},
        setValue,
        watch,
        reset,
    } = useForm<TagFormData>({
        resolver: zodResolver(tagSchema),
        defaultValues: {
            name: '',
            description: '',
            nfcTagUuid: '',
            lockedImageFilename: '',
            unlockedImageFilename: '',
            unlockImages: []
        }
    });

    const watchedUnlockImages = watch('unlockImages');

    useEffect(() => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (pos) => {
                    setMapCenter([pos.coords.latitude, pos.coords.longitude]);
                },
                (err) => {
                    console.warn("Geolocation error:", err);
                    // fallback remains [55.505, -0.09]
                }
            );
        }
    }, []);

    useEffect(() => {
        loadTags();
    }, []);

    const loadTags = async () => {
        try {
            const data = await getAllTagLocationsRequest();
            setTags(data);
        } catch (error) {
            console.error('Failed to load tags:', error);
        }
    };

    const onSubmit = async (data: TagFormData) => {
        if (editingTag) {
            // Update existing tag
            await toast.promise(
                (async () => {
                    const updated = await updateTagLocationRequest(editingTag, data as UpdateTagLocationDto);
                    setTags(prev => prev.map(tag => tag.id === editingTag ? updated : tag));
                    reset();
                    setEditingTag(null);
                    setExpandedTag(null);
                    return updated;
                })(),
                {
                    loading: t('updating_tag') || 'Updating tag...',
                    success: (updated) => t('tag_updated_successfully') || `Tag "${updated.name}" updated successfully`,
                    error: (err: any) => {
                        const apiMessage = err?.response?.data?.error;
                        return apiMessage || t('failed_to_update_tag') || 'Failed to update tag';
                    },
                }
            );
        } else {
            // Create new tag
            await toast.promise(
                (async () => {
                    const created = await createTagLocationRequest(data as CreateTagLocationDto);
                    setTags(prev => [created, ...prev]);
                    reset();
                    setIsCreating(false);
                    return created;
                })(),
                {
                    loading: t('creating_tag') || 'Creating tag...',
                    success: (created) => t('tag_created_successfully') || `Tag "${created.name}" created successfully`,
                    error: (err: any) => {
                        const apiMessage = err?.response?.data?.error;
                        return apiMessage || t('failed_to_create_tag') || 'Failed to create tag';
                    },
                }
            );
        }
    };

    const handleEdit = (tag: TagLocationResponseDto) => {
        setEditingTag(tag.id);
        setIsCreating(false);
        setExpandedTag(tag.id);

        // Populate form with tag data
        reset({
            name: tag.name,
            description: tag.description || '',
            nfcTagUuid: tag.nfcTagUuid,
            latitude: tag.latitude,
            longitude: tag.longitude,
            lockedImageFilename: tag.lockedImageFilename,
            unlockedImageFilename: tag.unlockedImageFilename,
            unlockImages: tag.unlockImages
        });
    };

    const handleCancelEdit = () => {
        setEditingTag(null);
        reset();
    };

    const handleDelete = async (id: string) => {
        if (!confirm(t('confirm_delete_tag') || 'Are you sure you want to delete this tag?')) {
            return;
        }

        await toast.promise(
            (async () => {
                await deleteTagLocationRequest(id);
                setTags(prev => prev.filter(tag => tag.id !== id));
                if (editingTag === id) {
                    setEditingTag(null);
                    reset();
                }
            })(),
            {
                loading: t('deleting_tag') || 'Deleting tag...',
                success: t('tag_deleted_successfully') || 'Tag deleted successfully',
                error: (err: any) => {
                    const apiMessage = err?.response?.data?.error;
                    return apiMessage || t('failed_to_delete_tag') || 'Failed to delete tag';
                },
            }
        );
    };

    const toggleExpand = (id: string) => {
        if (editingTag && editingTag !== id) {
            return; // Prevent expanding other cards while editing
        }
        setExpandedTag(expandedTag === id ? null : id);
    };

    const addUnlockImage = (tagId?: string) => {
        if (tagId) {
            // Add to existing tag during edit
            const tag = tags.find(t => t.id === tagId);
            if (tag) {
                const updated = {
                    ...tag,
                    unlockImages: [...tag.unlockImages, {imageFilename: '', displayOrder: tag.unlockImages.length + 1}]
                };
                setTags(prev => prev.map(t => t.id === tagId ? updated : t));
            }
        } else {
            // Add to new tag form
            const currentImages = watchedUnlockImages || [];
            setValue('unlockImages', [
                ...currentImages,
                {imageFilename: '', displayOrder: currentImages.length + 1}
            ]);
        }
    };

    const removeUnlockImage = (index: number, tagId?: string) => {
        if (tagId) {
            const tag = tags.find(t => t.id === tagId);
            if (tag) {
                const updated = {
                    ...tag,
                    unlockImages: tag.unlockImages.filter((_, i) => i !== index)
                };
                setTags(prev => prev.map(t => t.id === tagId ? updated : t));
            }
        } else {
            const currentImages = watchedUnlockImages || [];
            setValue('unlockImages', currentImages.filter((_, i) => i !== index));
        }
    };

    const updateUnlockImage = (index: number, field: keyof CreateTagLocationUnlockImageDto, value: string | number, tagId?: string) => {
        if (tagId) {
            const tag = tags.find(t => t.id === tagId);
            if (tag) {
                const updated = {
                    ...tag,
                    unlockImages: tag.unlockImages.map((img, i) =>
                        i === index ? {...img, [field]: value} : img
                    )
                };
                setTags(prev => prev.map(t => t.id === tagId ? updated : t));
            }
        } else {
            const currentImages = watchedUnlockImages || [];
            const updatedImages = currentImages.map((img, i) =>
                i === index ? {...img, [field]: value} : img
            );
            setValue('unlockImages', updatedImages);
        }
    };

    const isFormMode = isCreating || editingTag;

    return (
        <div className="md:p-4 max-w-5xl mx-auto">
            <div className="bg-white rounded-xl shadow md:p-6 p-4 space-y-4 border border-gray-200">
                {/* Header */}
                <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                    <div>
                        <h2 className="text-xl font-semibold text-gray-800">
                            {t('tags_management') || 'Tags Management'}
                        </h2>
                    </div>

                    <button
                        onClick={() => {
                            setIsCreating(true);
                            setEditingTag(null);
                            reset();
                        }}
                        className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50"
                    >
                        <Plus size={16}/>
                        {t('add_tag') || 'Add Tag'}
                    </button>
                </div>

                {/* Total */}
                <div className="text-sm text-gray-500">
                    {t('total_tags') || 'Total tags'}: {tags.length}
                </div>

                {/* Divider */}
                <hr className="border-gray-300"/>

                {/* Create/Edit Form */}
                {isFormMode && (
                    <form onSubmit={handleSubmit(onSubmit)}
                          className="border-2 border-dashed border-blue-300 rounded-lg p-4 sm:p-6 bg-blue-50 space-y-4">
                        <h3 className="font-semibold text-gray-800 flex items-center gap-2">
                            {editingTag ? <Edit size={16}/> : <Plus size={16}/>}
                            {editingTag ? (t('edit_tag') || 'Edit Tag') : (t('create_new_tag') || 'Create New Tag')}
                        </h3>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div className="md:col-span-2">
                                <input
                                    type="text"
                                    placeholder={t('tag_name') || 'Tag Name'}
                                    {...register('name')}
                                    className={`p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full ${errors.name ? 'border-red-500' : ''}`}
                                />
                                {errors.name && (
                                    <p className="text-red-500 text-sm mt-1">{t(errors.name.message!)}</p>
                                )}
                            </div>

                            <div className="md:col-span-2">
                                <div className="flex flex-col sm:flex-row gap-2">
                                    <input
                                        type="text"
                                        placeholder={t('nfc_tag_uuid') || 'NFC Tag UUID'}
                                        {...register('nfcTagUuid')}
                                        className={`flex-1 p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                                            errors.nfcTagUuid ? 'border-red-500' : ''
                                        }`}
                                    />
                                    <button
                                        type="button"
                                        onClick={() => setValue('nfcTagUuid', uuidv4(), {shouldValidate: true})}
                                        className="px-3 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
                                    >
                                        {t('generate') || 'Generate'}
                                    </button>
                                </div>
                                {errors.nfcTagUuid && (
                                    <p className="text-red-500 text-sm mt-1">{t(errors.nfcTagUuid.message!)}</p>
                                )}
                            </div>

                            <div>
                                <input
                                    type="number"
                                    step="any"
                                    placeholder={t('latitude') || 'Latitude'}
                                    {...register('latitude', {valueAsNumber: true})}
                                    className={`p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full ${errors.latitude ? 'border-red-500' : ''}`}
                                />
                                {errors.latitude && (
                                    <p className="text-red-500 text-sm mt-1">{t(errors.latitude.message!)}</p>
                                )}
                            </div>

                            <div>
                                <input
                                    type="number"
                                    step="any"
                                    placeholder={t('longitude') || 'Longitude'}
                                    {...register('longitude', {valueAsNumber: true})}
                                    className={`p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full ${errors.longitude ? 'border-red-500' : ''}`}
                                />
                                {errors.longitude && (
                                    <p className="text-red-500 text-sm mt-1">{t(errors.longitude.message!)}</p>
                                )}
                            </div>

                            <div className="md:col-span-2">
                                <label className="block text-sm font-semibold text-gray-700 mb-2">
                                    {t('location') || 'Select Location'}
                                </label>

                                <div className="h-64 w-full rounded-md overflow-hidden border">
                                    <MapContainer
                                        center={mapCenter}
                                        zoom={13}
                                        style={{height: "100%", width: "100%"}}
                                    >
                                        <TileLayer
                                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                                        />
                                        <LocationPicker setValue={setValue}/>
                                        {(watch("latitude") && watch("longitude")) && (
                                            <Marker
                                                position={[watch("latitude"), watch("longitude")]}
                                            />
                                        )}
                                    </MapContainer>
                                </div>
                            </div>

                        </div>

                        <div>
            <textarea
                placeholder={t('description') || 'Description'}
                {...register('description')}
                className={`w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${errors.description ? 'border-red-500' : ''}`}
                rows={3}
            />
                            {errors.description && (
                                <p className="text-red-500 text-sm mt-1">{t(errors.description.message!)}</p>
                            )}
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-semibold text-gray-700 mb-2">
                                    {t('locked_image') || 'Locked Image'}
                                </label>
                                <ImageUpload
                                    value={watch('lockedImageFilename') || ''}
                                    onChange={(filename) => setValue('lockedImageFilename', filename)}
                                    placeholder={t('upload_locked_image') || 'Upload locked state image'}
                                />
                                {errors.lockedImageFilename && (
                                    <p className="text-red-500 text-sm mt-1">{t(errors.lockedImageFilename.message!)}</p>
                                )}
                            </div>

                            <div>
                                <label className="block text-sm font-semibold text-gray-700 mb-2">
                                    {t('unlocked_image') || 'Unlocked Image'}
                                </label>
                                <ImageUpload
                                    value={watch('unlockedImageFilename') || ''}
                                    onChange={(filename) => setValue('unlockedImageFilename', filename)}
                                    placeholder={t('upload_unlocked_image') || 'Upload unlocked state image'}
                                />
                                {errors.unlockedImageFilename && (
                                    <p className="text-red-500 text-sm mt-1">{t(errors.unlockedImageFilename.message!)}</p>
                                )}
                            </div>
                        </div>

                        <div>
                            <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 mb-4">
                                <h4 className="font-semibold">{t('unlock_images') || 'Unlock Images'}</h4>
                                <button
                                    type="button"
                                    onClick={() => addUnlockImage()}
                                    className="text-blue-600 hover:text-blue-800 text-sm flex items-center gap-1 self-start sm:self-auto"
                                >
                                    <Plus size={14}/>
                                    {t('add_image') || 'Add Image'}
                                </button>
                            </div>

                            {errors.unlockImages && (
                                <p className="text-red-500 text-sm mt-1">{t(errors.unlockImages.message!)}</p>
                            )}

                            <div className="space-y-4">
                                {watchedUnlockImages?.map((img, index) => (
                                    <div key={index} className="border rounded-lg p-3 sm:p-4 bg-white">
                                        {/* Mobile: Stack vertically, Desktop: Use grid */}
                                        <div className="space-y-4">
                                            {/* Image Upload Section */}
                                            <div>
                                                <label className="block text-sm font-semibold text-gray-700 mb-2">
                                                    {t('image') || 'Image'} #{index + 1}
                                                </label>
                                                <ImageUpload
                                                    value={img.imageFilename}
                                                    onChange={(filename) => updateUnlockImage(index, 'imageFilename', filename)}
                                                    placeholder={t('upload_unlock_image') || 'Upload unlock image'}
                                                    showPreview={true}
                                                />
                                                {errors.unlockImages?.[index]?.imageFilename && (
                                                    <p className="text-red-500 text-xs mt-1">
                                                        {t(errors.unlockImages[index]?.imageFilename?.message!)}
                                                    </p>
                                                )}
                                            </div>

                                            {/* Order and Remove Section */}
                                            <div className="flex gap-2">
                                                {/* Order Input */}
                                                <div className="flex-1">
                                                    <label className="block text-sm font-semibold text-gray-700 mb-2">
                                                        {t('order') || 'Order'}
                                                    </label>
                                                    <input
                                                        type="number"
                                                        placeholder={t('order') || 'Order'}
                                                        value={img.displayOrder}
                                                        onChange={(e) => updateUnlockImage(index, 'displayOrder', parseInt(e.target.value) || 1)}
                                                        className="w-full p-2 border rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                                                    />
                                                    {errors.unlockImages?.[index]?.displayOrder && (
                                                        <p className="text-red-500 text-xs mt-1">
                                                            {t(errors.unlockImages[index]?.displayOrder?.message!)}
                                                        </p>
                                                    )}
                                                </div>

                                                {/* Remove Button */}
                                                <div className="flex items-end sm:items-start sm:mt-7 mb-1">
                                                    <button
                                                        type="button"
                                                        onClick={() => removeUnlockImage(index)}
                                                        className="p-2 text-red-600 hover:text-red-800 hover:bg-red-50 rounded border border-red-200 hover:border-red-300 transition-colors"
                                                        title={t('remove') || 'Remove'}
                                                    >
                                                        <X size={16}/>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>

                        <div className="flex flex-col sm:flex-row gap-2 pt-2">
                            <button
                                type="submit"
                                disabled={isSubmitting}
                                className="flex items-center justify-center gap-2 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 disabled:opacity-50 transition-colors"
                            >
                                <Save size={16}/>
                                {editingTag ? (t('update') || 'Update') : (t('create') || 'Create')}
                            </button>
                            <button
                                type="button"
                                onClick={() => {
                                    if (editingTag) {
                                        handleCancelEdit();
                                    } else {
                                        setIsCreating(false);
                                        reset();
                                    }
                                }}
                                className="flex items-center justify-center gap-2 px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors"
                            >
                                <X size={16}/>
                                {t('cancel') || 'Cancel'}
                            </button>
                        </div>
                    </form>
                )}

                {/* Tags Cards */}
                <div className="space-y-6">
                    {tags.map((tag) => {
                        const isOpen = expandedTag === tag.id;
                        const isBeingEdited = editingTag === tag.id;

                        return (
                            <div
                                key={tag.id}
                                className={`border rounded-lg shadow-sm transition-all ${
                                    isBeingEdited
                                        ? 'bg-blue-50 border-blue-200'
                                        : 'bg-gray-50 hover:bg-gray-100'
                                }`}
                            >
                                <div className="p-4">
                                    <div className="flex items-start justify-between gap-3">
                                        <div className="flex-1 min-w-0 cursor-pointer"
                                             onClick={() => !isFormMode && toggleExpand(tag.id)}>
                                            <h3 className="font-semibold text-lg text-gray-800 break-words">{tag.name}</h3>

                                            <div
                                                className="flex flex-col sm:flex-row sm:items-center gap-2 sm:gap-4 mt-2 text-sm text-gray-600">
                                                <span className="flex items-center gap-1">
                                                    <MapPin size={14}/>
                                                    <span className="truncate">
                                                        {tag.latitude.toFixed(4)}, {tag.longitude.toFixed(4)}
                                                    </span>
                                                </span>
                                                <span className="flex items-center gap-1">
                                                    <Image size={14}/>
                                                    {tag.unlockImages.length} {t('unlock_images') || 'unlock images'}
                                                </span>
                                            </div>
                                        </div>

                                        <div className="flex flex-col sm:flex-row items-center gap-1 flex-shrink-0">
                                            {!isBeingEdited && (
                                                <button
                                                    onClick={(e) => {
                                                        e.stopPropagation();
                                                        handleEdit(tag);
                                                    }}
                                                    className="p-2 text-blue-600 hover:text-blue-800 hover:bg-blue-50 rounded disabled:opacity-50"
                                                    title={t('edit') || 'Edit'}
                                                >
                                                    <Edit size={16}/>
                                                </button>
                                            )}

                                            <button
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    handleDelete(tag.id);
                                                }}
                                                className="p-2 text-red-600 hover:text-red-800 hover:bg-red-50 rounded disabled:opacity-50"
                                                title={t('delete') || 'Delete'}
                                            >
                                                <Trash2 size={16}/>
                                            </button>

                                            {!isBeingEdited && (
                                                <button
                                                    onClick={() => toggleExpand(tag.id)}
                                                    className="p-2 text-gray-600 hover:text-gray-800 hover:bg-gray-100 rounded disabled:opacity-50"
                                                    title={isOpen ? t('collapse') || 'Collapse' : t('expand') || 'Expand'}
                                                >
                                                    {isOpen ? <ChevronUp size={16}/> : <ChevronDown size={16}/>}
                                                </button>
                                            )}
                                        </div>
                                    </div>
                                </div>

                                {/* Expanded Details - Hide when being edited since form is shown above */}
                                {isOpen && !isBeingEdited && (
                                    <div className="p-4 text-sm text-gray-700 space-y-4 border-t">
                                        <div className="space-y-2">
                                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                                <div>
                                                        <span
                                                            className="font-semibold text-gray-700">{t('nfc_uuid') || 'NFC UUID'}:</span>
                                                    <p className="text-gray-600 break-all">{tag.nfcTagUuid}</p>
                                                </div>

                                                <div>
                                                        <span
                                                            className="font-semibold text-gray-700">{t('coordinates') || 'Coordinates'}:</span>
                                                    <p className="text-gray-600">
                                                        {tag.latitude.toFixed(6)}, {tag.longitude.toFixed(6)}
                                                        <button
                                                            onClick={() => window.open(`https://maps.google.com?q=${tag.latitude},${tag.longitude}`, '_blank')}
                                                            className="ml-2 text-blue-600 hover:text-blue-800"
                                                            title={t('view_on_map') || 'View on map'}
                                                        >
                                                            <ExternalLink size={12} className="inline"/>
                                                        </button>
                                                    </p>
                                                </div>
                                            </div>

                                            {tag.description && (
                                                <div>
                                                        <span
                                                            className="font-semibold text-gray-700">{t('description') || 'Description'}:</span>
                                                    <p className="text-gray-600 mt-1">{tag.description}</p>
                                                </div>
                                            )}

                                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                                <div>
                                                        <span
                                                            className="font-semibold text-gray-700">{t('locked_image') || 'Locked Image'}:</span>
                                                    {tag.lockedImageFilename ? (
                                                        <ImageUpload
                                                            value={tag.lockedImageFilename}
                                                            onChange={() => {
                                                            }} // Read-only in view mode
                                                            disabled={true}
                                                            className="mt-2"
                                                        />
                                                    ) : (
                                                        <p className="text-gray-500 italic">{t('no_image') || 'No image'}</p>
                                                    )}
                                                </div>

                                                <div>
                                                        <span
                                                            className="font-semibold text-gray-700">{t('unlocked_image') || 'Unlocked Image'}:</span>
                                                    {tag.unlockedImageFilename ? (
                                                        <ImageUpload
                                                            value={tag.unlockedImageFilename}
                                                            onChange={() => {
                                                            }} // Read-only in view mode
                                                            disabled={true}
                                                            className="mt-2"
                                                        />
                                                    ) : (
                                                        <p className="text-gray-500 italic">{t('no_image') || 'No image'}</p>
                                                    )}
                                                </div>
                                            </div>

                                            {tag.unlockImages.length > 0 && (
                                                <div>
                                                        <span
                                                            className="font-semibold text-gray-700">{t('unlock_images') || 'Unlock Images'}:</span>
                                                    <div
                                                        className="mt-2 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                                                        {tag.unlockImages
                                                            .sort((a, b) => a.displayOrder - b.displayOrder)
                                                            .map((img, index) => (
                                                                <div key={index} className="space-y-2">
                                                                    <div className="flex items-center gap-2">
                                                                            <span
                                                                                className="w-6 h-6 bg-gray-200 rounded-full flex items-center justify-center text-xs font-semibold">
                                                                                {img.displayOrder}
                                                                            </span>
                                                                        <span
                                                                            className="text-sm text-gray-600 truncate">{img.imageFilename}</span>
                                                                    </div>
                                                                    {img.imageFilename && (
                                                                        <ImageUpload
                                                                            value={img.imageFilename}
                                                                            onChange={() => {
                                                                            }} // Read-only in view mode
                                                                            disabled={true}
                                                                        />
                                                                    )}
                                                                </div>
                                                            ))}
                                                    </div>
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                )}
                            </div>
                        );
                    })}
                </div>

                {/* Empty State */}
                {tags.length === 0 && !isCreating && (
                    <div className="text-center py-12 text-gray-500">
                        <Nfc size={48} className="mx-auto mb-4 opacity-50"/>
                        <p className="text-lg">{t('no_tags_found') || 'No tags found'}</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default TagsManagement;