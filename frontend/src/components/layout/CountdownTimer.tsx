import {useState, useEffect} from 'react';
import {useTranslation} from 'react-i18next';

interface TimeLeft {
    days: number;
    hours: number;
    minutes: number;
    seconds: number;
}

interface CountdownTimerProps {
    targetDate: string;
    targetLocation: string;
    albumTitle?: string;
    albumLink?: string;
    ticketLink?: string;
    showEventDate?: boolean;
    showEventLocation?: boolean;
    eventDateFormat?: string;
    className?: string;
    onComplete?: () => void;
}

export default function CountdownTimer({
                                           targetDate,
                                           targetLocation,
                                           albumTitle,
                                           albumLink,
                                           ticketLink,
                                           showEventDate = true,
                                           showEventLocation = true,
                                           eventDateFormat,
                                           className = "",
                                           onComplete
                                       }: CountdownTimerProps) {
    const {t} = useTranslation();

    const calculateTimeLeft = (): TimeLeft => {
        const now = new Date();
        const target = new Date(targetDate);
        const difference = target.getTime() - now.getTime();

        if (difference > 0) {
            return {
                days: Math.floor(difference / (1000 * 60 * 60 * 24)),
                hours: Math.floor((difference / (1000 * 60 * 60)) % 24),
                minutes: Math.floor((difference / 1000 / 60) % 60),
                seconds: Math.floor((difference / 1000) % 60)
            };
        } else {
            return {days: 0, hours: 0, minutes: 0, seconds: 0};
        }
    };

    const [timeLeft, setTimeLeft] = useState<TimeLeft>(calculateTimeLeft());

    const isCompleted = Object.values(timeLeft).every((v) => v === 0);

    useEffect(() => {
        if (isCompleted) {
            onComplete && onComplete();
            return;
        }

        const timer = setInterval(() => {
            setTimeLeft(calculateTimeLeft());
        }, 1000);

        return () => clearInterval(timer);
    }, [isCompleted, targetDate, onComplete]);

    const formatEventDate = (dateString: string) => {
        if (eventDateFormat) return eventDateFormat;
        const date = new Date(dateString);
        return date.toLocaleDateString('pl-PL', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const timeUnits = [
        {value: timeLeft.days, label: t('days'), key: 'days'},
        {value: timeLeft.hours, label: t('hours'), key: 'hours'},
        {value: timeLeft.minutes, label: t('minutes'), key: 'minutes'},
        {value: timeLeft.seconds, label: t('seconds'), key: 'seconds'}
    ];

    return (
        <div className={`w-full max-w-2xl ${className}`}>
            {!isCompleted ? (
                <>
                    <div className="grid grid-cols-4 sm:gap-6 px-6">
                        {timeUnits.map(({value, label, key}) => (
                            <div
                                key={key}
                                className="p-4 sm:p-6"
                            >
                                <div className="text-2xl md:text-4xl mb-1 font-crimson"
                                >
                                    {value.toString().padStart(2, '0')}
                                </div>
                                <div className="text-xs sm:text-sm md:text-base lowercase tracking-wide font-crimson"
                                >
                                    {label}
                                </div>
                            </div>
                        ))}
                    </div>

                    <div className="mt-8">
                        {showEventDate && (
                            <div className="mt-4 md:text-xl text-gray-800 text-center font-crimson"
                            >
                                {formatEventDate(targetDate)}
                            </div>
                        )}

                        {showEventLocation && (
                            <div className="mt-2 md:text-xl text-gray-800 text-center font-crimson"
                            >
                                {targetLocation}
                            </div>
                        )}

                        {ticketLink && (
                            <div className="mt-2  md:text-xl text-gray-800 text-center font-crimson"
                            >
                                <a
                                    href={ticketLink}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    className="hover:underline"
                                >
                                    {t('buyTickets')}
                                </a>
                            </div>
                        )}
                    </div>

                </>
            ) : (
                <div className="mt-6 text-center">
                    {albumTitle && albumLink && (
                        <a
                            href={albumLink}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="text-lg sm:text-xl md:text-2xl font-semibold text-blue-600 hover:underline font-crimson"
                        >
                            {albumTitle}
                        </a>
                    )}
                </div>
            )}
        </div>
    );
}