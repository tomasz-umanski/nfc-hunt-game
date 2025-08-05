import {useTranslation} from 'react-i18next';
import HamburgerMenu from "@/components/layout/HamburgerMenu.tsx";
import {useForm} from 'react-hook-form';
import {zodResolver} from '@hookform/resolvers/zod';
import * as z from 'zod';
import toast from 'react-hot-toast';
import {useAuthStore} from '@/store/authStore';
import {useNavigate} from 'react-router-dom';
import {Eye, EyeOff} from 'lucide-react';
import {useState} from 'react';
import {changePasswordRequest, deleteAccountRequest} from "@/api/profile/profile.ts";
import {Trash2, X} from 'lucide-react';

const schema = z.object({
    currentPassword: z.string().min(1, 'validation.currentPassword.required'),
    newPassword: z.string().min(8, 'validation.password.size').max(100, 'validation.password.size'),
    confirmNewPassword: z
        .string()
        .max(100, 'validation.confirmPassword.size'),
}).refine((data) => data.newPassword === data.confirmNewPassword, {
    message: 'validation.passwords.match',
    path: ['confirmNewPassword'],
});

type FormData = z.infer<typeof schema>;

export default function AccountSettingsPage() {
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

    const {t} = useTranslation();
    const navigate = useNavigate();
    const logout = useAuthStore((state) => state.logout);

    const {
        register,
        handleSubmit,
        formState: {errors, isSubmitting},
    } = useForm<FormData>({resolver: zodResolver(schema)});

    const [showCurrent, setShowCurrent] = useState(false);
    const [showNew, setShowNew] = useState(false);
    const [showConfirm, setShowConfirm] = useState(false);

    const onChangePassword = async (data: FormData) => {
        const {refreshToken} = useAuthStore.getState();

        if (!refreshToken) {
            logout();
            navigate('/login');
            return;
        }

        try {
            await toast.promise(
                changePasswordRequest({
                    currentPassword: data.currentPassword,
                    newPassword: data.newPassword,
                    confirmNewPassword: data.confirmNewPassword,
                    refreshToken: refreshToken
                }),
                {
                    loading: t('password_change_loading'),
                    success: t('password_change_success'),
                    error: (err) =>
                        t('password_change_error') + ' ' + err?.response?.data?.error || t('password_change_error'),
                }
            );

            logout();
            navigate('/');
        } catch (err: any) {
            if (err?.response?.status === 401) {
                logout();
                navigate('/login');
            }
        }
    };

    const confirmDeleteAccount = async () => {
        try {
            await toast.promise(
                deleteAccountRequest(),
                {
                    loading: t('account_deletion_loading'),
                    success: () => {
                        logout();
                        navigate('/');
                        return t('account_deletion_success');
                    },
                    error: (err) =>
                        t('account_deletion_error') + ' ' + err?.response?.data?.error || t('account_deletion_error'),
                }
            );
        } catch (err: any) {
            if (err?.response?.status === 401) {
                logout();
                navigate('/login');
            }
        }
    };

    return (
        <div className="min-h-screen flex flex-col justify-center items-center p-4 bg-gray-100">
            <HamburgerMenu/>
            {isDeleteModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
                    <div className="bg-white p-6 rounded shadow-lg max-w-sm w-full relative">
                        <button
                            onClick={() => setIsDeleteModalOpen(false)}
                            className="absolute top-3 right-3 text-gray-500 hover:text-gray-700"
                        >
                            <X size={20}/>
                        </button>

                        <h2 className="text-lg font-semibold mb-4 flex items-center gap-2">
                            <Trash2 size={20} className="text-red-600"/>
                            {t('delete_account')}
                        </h2>

                        <p className="mb-6 text-sm text-gray-700">
                            {t('delete_account_confirm')}
                        </p>

                        <div className="flex justify-end gap-2">
                            <button
                                onClick={() => setIsDeleteModalOpen(false)}
                                className="px-4 py-2 bg-gray-200 text-sm rounded hover:bg-gray-300"
                            >
                                {t('cancel')}
                            </button>
                            <button
                                onClick={async () => {
                                    setIsDeleteModalOpen(false);
                                    await confirmDeleteAccount();
                                }}
                                className="px-4 py-2 bg-red-600 text-white text-sm rounded hover:bg-red-700"
                            >
                                {t('delete_account')}
                            </button>
                        </div>
                    </div>
                </div>
            )}
            <div className="w-full max-w-md bg-white p-6 rounded shadow space-y-6">
                <h1 className="text-2xl font-bold text-center">{t('account_settings')}</h1>

                <form onSubmit={handleSubmit(onChangePassword)} className="space-y-4">
                    <div className="relative">
                        <label className="block text-sm font-medium mb-1">{t('current_password')}</label>
                        <input
                            type={showCurrent ? 'text' : 'password'}
                            {...register('currentPassword')}
                            className="w-full border p-2 pr-10 rounded"
                        />
                        <button
                            type="button"
                            onClick={() => setShowCurrent(prev => !prev)}
                            className="absolute top-9 right-3 text-gray-500 hover:text-gray-700"
                            tabIndex={-1}
                            aria-label={showCurrent ? t('hide_password') : t('show_password')}
                        >
                            {showCurrent ? <EyeOff size={20}/> : <Eye size={20}/>}
                        </button>
                        {errors.currentPassword && (
                            <p className="text-red-500 text-sm mt-1">{t(errors.currentPassword.message!)}</p>
                        )}
                    </div>

                    <div className="relative">
                        <label className="block text-sm font-medium mb-1">{t('new_password')}</label>
                        <input
                            type={showNew ? 'text' : 'password'}
                            {...register('newPassword')}
                            className="w-full border p-2 pr-10 rounded"
                        />
                        <button
                            type="button"
                            onClick={() => setShowNew(prev => !prev)}
                            className="absolute top-9 right-3 text-gray-500 hover:text-gray-700"
                            tabIndex={-1}
                            aria-label={showNew ? t('hide_password') : t('show_password')}
                        >
                            {showNew ? <EyeOff size={20}/> : <Eye size={20}/>}
                        </button>
                        {errors.newPassword && (
                            <p className="text-red-500 text-sm mt-1">{t(errors.newPassword.message!)}</p>
                        )}
                    </div>

                    <div className="relative">
                        <label className="block text-sm font-medium mb-1">{t('confirm_new_password')}</label>
                        <input
                            type={showConfirm ? 'text' : 'password'}
                            {...register('confirmNewPassword')}
                            className="w-full border p-2 pr-10 rounded"
                        />
                        <button
                            type="button"
                            onClick={() => setShowConfirm(prev => !prev)}
                            className="absolute top-9 right-3 text-gray-500 hover:text-gray-700"
                            tabIndex={-1}
                            aria-label={showConfirm ? t('hide_password') : t('show_password')}
                        >
                            {showConfirm ? <EyeOff size={20}/> : <Eye size={20}/>}
                        </button>
                        {errors.confirmNewPassword && (
                            <p className="text-red-500 text-sm mt-1">{t(errors.confirmNewPassword.message!)}</p>
                        )}
                    </div>

                    <button
                        type="submit"
                        disabled={isSubmitting}
                        className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 disabled:opacity-50"
                    >
                        {t('change_password')}
                    </button>
                </form>

                <button
                    onClick={() => setIsDeleteModalOpen(true)}
                    className="w-full bg-red-600 text-white py-2 rounded hover:bg-red-700 flex items-center justify-center gap-2"
                >
                    <Trash2 size={18}/>
                    {t('delete_account')}
                </button>
            </div>
        </div>
    );
}
