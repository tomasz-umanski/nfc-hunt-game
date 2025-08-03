import {useForm} from 'react-hook-form';
import {zodResolver} from '@hookform/resolvers/zod';
import * as z from 'zod';
import {useTranslation} from 'react-i18next';
import {useNavigate} from 'react-router-dom';
import {useAuthStore} from '../store/authStore';
import {loginRequest} from '@/api/auth/auth.ts';
import toast from 'react-hot-toast';

const schema = z.object({
    email: z.string().email('validation.email.format').max(100, 'validation.email.size'),
    password: z.string().min(8, 'validation.password.size').max(100, 'validation.password.size'),
});

type FormData = z.infer<typeof schema>;

export default function LoginPage() {
    const {t} = useTranslation();
    const {
        register,
        handleSubmit,
        formState: {errors, isSubmitting},
    } = useForm<FormData>({resolver: zodResolver(schema)});

    const login = useAuthStore((state) => state.login);
    const navigate = useNavigate();

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

                <div>
                    <label className="block text-sm font-medium mb-1">{t('password')}</label>
                    <input
                        type="password"
                        {...register('password')}
                        className="w-full p-2 border border-gray-300 rounded"
                    />
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
            </form>
        </div>
    );
}
