import {useState} from 'react';
import {useForm} from 'react-hook-form';
import {zodResolver} from '@hookform/resolvers/zod';
import * as z from 'zod';
import {Trans, useTranslation} from 'react-i18next';
import {Link, useNavigate} from 'react-router-dom';
import {useAuthStore} from '../store/authStore';
import {registerRequest} from '@/api/auth/auth.ts';
import toast from 'react-hot-toast';
import {Eye, EyeOff} from 'lucide-react';
import HamburgerMenu from "@/components/layout/HamburgerMenu.tsx";

const schema = z
    .object({
        email: z.email('validation.email.format').max(100, 'validation.email.size'),
        password: z.string().min(8, 'validation.password.size').max(100, 'validation.password.size'),
        confirmPassword: z.string().max(100, 'validation.confirmPassword.size'),
        acceptGameRules: z.boolean().refine(val => val === true),
        acceptPrivacyPolicy: z.boolean().refine(val => val === true),
        confirmAge: z.boolean().refine(val => val === true),
    })
    .refine((data) => data.password === data.confirmPassword, {
        message: 'validation.passwords.match',
        path: ['confirmPassword'],
    });

type FormData = z.infer<typeof schema>;

export default function RegisterPage() {
    const {t} = useTranslation();
    const {
        register,
        handleSubmit,
        formState: {errors, isSubmitting},
    } = useForm<FormData>({resolver: zodResolver(schema)});

    const login = useAuthStore((state) => state.login);
    const navigate = useNavigate();

    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const toggleShowPassword = () => setShowPassword((prev) => !prev);
    const toggleShowConfirmPassword = () => setShowConfirmPassword((prev) => !prev);

    const onSubmit = async (data: FormData) => {
        await toast.promise(
            (async () => {
                const auth = await registerRequest(data);
                login(auth);

                const role = useAuthStore.getState().user?.role;
                navigate(role === 'ADMIN' ? '/admin' : '/');
            })(),
            {
                loading: t('register_loading'),
                success: t('register_success'),
                error: (err: any) => {
                    const apiMessage = err?.response?.data?.error;
                    return apiMessage || t('register_error');
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
                <h2 className="text-xl font-semibold text-center">{t('register')}</h2>

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

                {/* Password Field */}
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
                        {showPassword ? <EyeOff size={20}/> : <Eye size={20}/>}
                    </button>
                    {errors.password && (
                        <p className="text-sm text-red-500 mt-1">{t(errors.password.message!)}</p>
                    )}
                </div>

                {/* Confirm Password Field */}
                <div className="relative">
                    <label className="block text-sm font-medium mb-1">{t('confirmPassword')}</label>
                    <input
                        type={showConfirmPassword ? 'text' : 'password'}
                        {...register('confirmPassword')}
                        className="w-full p-2 border border-gray-300 rounded pr-10"
                    />
                    <button
                        type="button"
                        onClick={toggleShowConfirmPassword}
                        className="absolute top-9 right-3 text-gray-500 hover:text-gray-700 focus:outline-none"
                        tabIndex={-1}
                        aria-label={showConfirmPassword ? t('hide_password') : t('show_password')}
                    >
                        {showConfirmPassword ? <EyeOff size={20}/> : <Eye size={20}/>}
                    </button>
                    {errors.confirmPassword && (
                        <p className="text-sm text-red-500 mt-1">{t(errors.confirmPassword.message!)}</p>
                    )}
                </div>

                <label
                    className={`inline-flex items-start space-x-2 cursor-pointer ${errors.acceptGameRules ? 'text-red-600' : ''}`}>
                    <input
                        type="checkbox"
                        {...register('acceptGameRules')}
                        className={`mt-1 ${errors.acceptGameRules ? 'border-red-600 ring-red-600' : ''}`}
                    />
                    <span className="text-sm">
                        <Trans
                            i18nKey="acceptGameRules"
                            components={{
                                termsLink: <a href="/terms" target="_blank" className="text-blue-600 underline"/>
                            }}
                        />
                    </span>
                </label>

                <label
                    className={`inline-flex items-start space-x-2 cursor-pointer ${errors.acceptPrivacyPolicy ? 'text-red-600' : ''}`}>
                    <input
                        type="checkbox"
                        {...register('acceptPrivacyPolicy')}
                        className={`mt-1 ${errors.acceptPrivacyPolicy ? 'border-red-600 ring-red-600' : ''}`}
                    />
                    <span className="text-sm">
                        <Trans
                            i18nKey="acceptPrivacyPolicy"
                            components={{
                                privacyLink: <a href="/privacy" target="_blank" className="text-blue-600 underline"/>
                            }}
                        />
                      </span>
                </label>

                <label
                    className={`inline-flex items-start space-x-2 cursor-pointer ${errors.confirmAge ? 'text-red-600' : ''}`}>
                    <input
                        type="checkbox"
                        {...register('confirmAge')}
                        className={`mt-1 ${errors.confirmAge ? 'border-red-600 ring-red-600' : ''}`}
                    />
                    <span className="text-sm">{t('confirmAge')}</span>
                </label>

                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700 disabled:opacity-50"
                >
                    {t('register')}
                </button>

                <p className="text-center text-sm mt-3">
                    {t('already_have_account')}{' '}
                    <Link to="/login" className="text-blue-600 hover:underline">
                        {t('login')}
                    </Link>
                </p>
            </form>
        </div>
    );
}