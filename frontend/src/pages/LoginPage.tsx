import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';
import { loginRequest } from '@/api/auth/auth.ts';
import toast from 'react-hot-toast';
import { Eye, EyeOff } from 'lucide-react';
import HamburgerMenu from "@/components/layout/HamburgerMenu.tsx"; // import icons

const schema = z.object({
    email: z.string().email('validation.email.format').max(100, 'validation.email.size'),
    password: z.string().min(8, 'validation.password.size').max(100, 'validation.password.size'),
});

type FormData = z.infer<typeof schema>;

export default function LoginPage() {
    const { t } = useTranslation();
    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm<FormData>({ resolver: zodResolver(schema) });

    const login = useAuthStore((state) => state.login);
    const navigate = useNavigate();

    const [showPassword, setShowPassword] = useState(false);

    const toggleShowPassword = () => setShowPassword((prev) => !prev);

    const onSubmit = async (data: FormData) => {
        await toast.promise(
            (async () => {
                const auth = await loginRequest(data);
                login(auth);

                const role = useAuthStore.getState().user?.role;
                navigate(role === 'ADMIN' ? '/admin' : '/');
            })(),
            {
                loading: t('login_loading'),
                success: t('login_success'),
                error: (err: any) => {
                    const apiMessage = err?.response?.data?.error;
                    return apiMessage || t('login_error');
                },
            }
        );
    };

    return (
        <div className="min-h-screen flex flex-col justify-center items-center p-4 bg-gray-100">
            <HamburgerMenu/>
            <form
                onSubmit={handleSubmit(onSubmit)}
                className="w-full max-w-sm bg-white shadow-md rounded p-6 flex flex-col gap-4"
            >
                <h2 className="text-xl font-semibold text-center">{t('login')}</h2>

                <div>
                    <label className="block text-sm font-medium mb-1">{t('email')}</label>
                    <input
                        type="email"
                        {...register('email')}
                        className="w-full p-2 border border-gray-300 rounded"
                    />
                    {errors.email && (
                        <p className="text-sm text-red-500 mt-1">{t(errors.email.message!)}</p>
                    )}
                </div>

                <div className="relative">
                    <label className="block text-sm font-medium mb-1">{t('password')}</label>
                    <input
                        type={showPassword ? 'text' : 'password'}
                        {...register('password')}
                        className="w-full p-2 border border-gray-300 rounded pr-10"
                    />
                    <button
                        type="button"
                        onClick={toggleShowPassword}
                        className="absolute top-9 right-3 text-gray-500 hover:text-gray-700 focus:outline-none"
                        tabIndex={-1}
                        aria-label={showPassword ? t('hide_password') : t('show_password')}
                    >
                        {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                    </button>
                    {errors.password && (
                        <p className="text-sm text-red-500 mt-1">{t(errors.password.message!)}</p>
                    )}
                </div>

                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700 disabled:opacity-50"
                >
                    {t('login')}
                </button>

                <p className="text-center text-sm mt-3">
                    {t('no_account')}{' '}
                    <Link to="/register" className="text-blue-600 hover:underline">
                        {t('register')}
                    </Link>
                </p>
            </form>
        </div>
    );
}