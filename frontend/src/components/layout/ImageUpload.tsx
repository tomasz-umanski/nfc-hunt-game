import React, { useRef, useState } from 'react';
import { Upload, X, Image, Loader2, ZoomIn } from 'lucide-react';
import { uploadFileRequest, getImageUrl } from '@/api/upload/upload';

interface ImageUploadProps {
    value: string;
    onChange: (filename: string) => void;
    onClear?: () => void;
    placeholder?: string;
    className?: string;
    accept?: string;
    maxSizeInMB?: number;
    disabled?: boolean;
    showPreview?: boolean;
}

const ImageUpload: React.FC<ImageUploadProps> = ({
                                                     value,
                                                     onChange,
                                                     onClear,
                                                     placeholder = 'Click to upload image or drag and drop',
                                                     className = '',
                                                     accept = 'image/*',
                                                     maxSizeInMB = 10,
                                                     disabled = false,
                                                     showPreview = true,
                                                 }) => {
    const fileInputRef = useRef<HTMLInputElement>(null);
    const [isUploading, setIsUploading] = useState(false);
    const [dragActive, setDragActive] = useState(false);
    const [error, setError] = useState<string>('');
    const [previewOpen, setPreviewOpen] = useState(false);

    const validateFile = (file: File): string | null => {
        const maxSizeInBytes = maxSizeInMB * 1024 * 1024;
        if (file.size > maxSizeInBytes) return `File size must be less than ${maxSizeInMB}MB`;
        if (accept === 'image/*' && !file.type.startsWith('image/')) return 'Please select a valid image file';
        return null;
    };

    const handleFileUpload = async (file: File) => {
        const validationError = validateFile(file);
        if (validationError) {
            setError(validationError);
            return;
        }

        setError('');
        setIsUploading(true);

        try {
            const response = await uploadFileRequest(file);
            onChange(response.fileUrl);
        } catch (err) {
            console.error('Upload failed:', err);
            setError('Upload failed. Please try again.');
        } finally {
            setIsUploading(false);
        }
    };

    const handleClick = () => !disabled && fileInputRef.current?.click();

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (file) handleFileUpload(file);
        if (fileInputRef.current) fileInputRef.current.value = '';
    };

    const handleDrag = (e: React.DragEvent) => {
        e.preventDefault(); e.stopPropagation();
    };

    const handleDragIn = (e: React.DragEvent) => {
        e.preventDefault(); e.stopPropagation();
        if (!disabled && e.dataTransfer.items?.length) setDragActive(true);
    };

    const handleDragOut = (e: React.DragEvent) => {
        e.preventDefault(); e.stopPropagation(); setDragActive(false);
    };

    const handleDrop = (e: React.DragEvent) => {
        e.preventDefault(); e.stopPropagation(); setDragActive(false);
        if (!disabled && e.dataTransfer.files?.length) handleFileUpload(e.dataTransfer.files[0]);
    };

    const handleClear = () => {
        onClear ? onClear() : onChange('');
        setError('');
    };

    const hasImage = value?.trim() !== '';

    return (
        <div className={`space-y-2 ${className}`}>
            <input
                ref={fileInputRef}
                type="file"
                accept={accept}
                onChange={handleFileChange}
                className="hidden"
                disabled={disabled}
            />

            {/* Upload Area */}
            <div
                onClick={handleClick}
                onDrag={handleDrag}
                onDragStart={handleDrag}
                onDragEnd={handleDrag}
                onDragOver={handleDrag}
                onDragEnter={handleDragIn}
                onDragLeave={handleDragOut}
                onDrop={handleDrop}
                className={`
          relative border-2 border-dashed rounded-lg p-4 transition-all cursor-pointer
          ${dragActive ? 'border-blue-400 bg-blue-50' : hasImage ? 'border-green-300 bg-green-50' : 'border-gray-300 bg-gray-50 hover:border-gray-400 hover:bg-gray-100'}
          ${disabled ? 'opacity-50 cursor-not-allowed' : ''}
          ${isUploading ? 'pointer-events-none' : ''}
        `}
            >
                {isUploading ? (
                    <div className="flex items-center justify-center py-8">
                        <Loader2 className="animate-spin text-blue-600" size={24} />
                        <span className="ml-2 text-sm text-gray-600">Uploading...</span>
                    </div>
                ) : hasImage ? (
                    <div className="flex items-center justify-between">
                        <div className="flex items-center space-x-2">
                            <Image size={20} className="text-green-600" />
                            <span className="text-sm text-gray-700 w-40 truncate">{value}</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <button
                                onClick={(e) => {
                                    e.stopPropagation();
                                    setPreviewOpen(true);
                                }}
                                className="p-1 hover:bg-gray-200 rounded-full text-gray-700 transition-colors"
                                title="Preview"
                            >
                                <ZoomIn size={16} />
                            </button>
                            <button
                                onClick={(e) => {
                                    e.stopPropagation();
                                    handleClear();
                                }}
                                className="p-1 hover:bg-red-100 rounded-full text-red-600 hover:text-red-800 transition-colors"
                                title="Remove image"
                            >
                                <X size={16} />
                            </button>
                        </div>
                    </div>
                ) : (
                    <div className="flex flex-col items-center justify-center py-8 text-gray-500">
                        <Upload size={32} className="mb-2" />
                        <p className="text-sm text-center">{placeholder}</p>
                        <p className="text-xs text-gray-400 mt-1">Max size: {maxSizeInMB}MB</p>
                    </div>
                )}
            </div>

            {/* Error Message */}
            {error && (
                <p className="text-sm text-red-600 flex items-center space-x-1">
                    <X size={14} />
                    <span>{error}</span>
                </p>
            )}

            {/* Image Preview */}
            {showPreview && hasImage && !isUploading && (
                <div className="mt-2">
                    <img
                        src={getImageUrl(value)}
                        alt="Preview"
                        className="max-w-full h-32 object-cover rounded border cursor-pointer"
                        onClick={() => setPreviewOpen(true)}
                        onError={(e) => ((e.target as HTMLImageElement).style.display = 'none')}
                    />
                </div>
            )}

            {/* Modal for enlarged preview */}
            {previewOpen && hasImage && (
                <div
                    className="fixed inset-0 z-50 bg-black/70 flex items-center justify-center p-4"
                    onClick={() => setPreviewOpen(false)}
                >
                    <img
                        src={getImageUrl(value)}
                        alt="Full Preview"
                        className="max-h-full max-w-full rounded shadow-lg"
                        onClick={(e) => e.stopPropagation()} // prevent closing when clicking the image
                    />
                    <button
                        onClick={() => setPreviewOpen(false)}
                        className="absolute top-4 right-4 text-white bg-black/50 rounded-full p-2 hover:bg-black/70 transition"
                    >
                        <X size={24} />
                    </button>
                </div>
            )}
        </div>
    );
};

export default ImageUpload;