import {useEffect, useState} from 'react';
import {useTranslation} from 'react-i18next';
import {
    ChevronDown,
    ChevronLeft,
    ChevronRight,
    ChevronUp,
    Mail,
    MapPin,
    Calendar,
    User
} from 'lucide-react';

import {getUserSummaryRequest} from '@/api/userSummary/userSummary.ts';
import type {UserSummary} from '@/types/userSummary';

const formatDate = (dateStr: string) => new Date(dateStr).toLocaleString();

export default function UserManagement() {
    const {t} = useTranslation();

    const [users, setUsers] = useState<UserSummary[]>([]);
    const [filtered, setFiltered] = useState<UserSummary[]>([]);
    const [search, setSearch] = useState('');
    const [sortField, setSortField] = useState<'tags' | 'created' | 'unlocked'>('tags');
    const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('desc');
    const [pageSize, setPageSize] = useState(5);
    const [page, setPage] = useState(1);
    const [expanded, setExpanded] = useState<string | null>(null);

    useEffect(() => {
        const fetch = async () => {
            const data = await getUserSummaryRequest();
            setUsers(data);
        };
        fetch();
    }, []);

    useEffect(() => {
        let result = [...users];

        if (search.trim()) {
            result = result.filter((u) =>
                u.email.toLowerCase().includes(search.toLowerCase())
            );
        }

        result.sort((a, b) => {
            const direction = sortOrder === 'asc' ? 1 : -1;

            if (sortField === 'tags') {
                return direction * (a.accessedTagsCount - b.accessedTagsCount);
            }

            if (sortField === 'created') {
                return direction * (new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime());
            }

            if (sortField === 'unlocked') {
                const getLatest = (u: UserSummary) =>
                    u.userAccessedTagDtoList.length > 0
                        ? new Date(Math.max(...u.userAccessedTagDtoList.map(tag => new Date(tag.unlockedAt).getTime())))
                        : new Date(0);

                return direction * (getLatest(a).getTime() - getLatest(b).getTime());
            }

            return 0;
        });

        setFiltered(result);
        setPage(1);
    }, [search, sortOrder, sortField, users]);

    const paginated = filtered.slice((page - 1) * pageSize, page * pageSize);
    const totalPages = Math.ceil(filtered.length / pageSize);

    return (
        <div className="md:p-4 max-w-5xl mx-auto">
            <div className="bg-white rounded-xl shadow md:p-6 p-4 space-y-4 border border-gray-200">
                {/* Header */}
                <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                    <input
                        type="text"
                        placeholder={t('filter_by_email') || 'Filter by email'}
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                        className="w-full md:w-1/3 p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />

                    <div className="flex flex-col md:flex-row gap-2 w-full md:w-auto">
                        <select
                            value={sortField}
                            onChange={(e) => setSortField(e.target.value as any)}
                            className="p-2 border rounded-md"
                        >
                            <optgroup label={t('sort_by')}>
                                <option value="tags">{t('tags_count')}</option>
                                <option value="created">{t('creation_date')}</option>
                                <option value="unlocked">{t('last_unlocked')}</option>
                            </optgroup>
                        </select>

                        <select
                            value={sortOrder}
                            onChange={(e) => setSortOrder(e.target.value as 'asc' | 'desc')}
                            className="p-2 border rounded-md"
                        >
                            <option value="desc">{t('descending')}</option>
                            <option value="asc">{t('ascending')}</option>
                        </select>

                        <select
                            value={pageSize}
                            onChange={(e) => setPageSize(Number(e.target.value))}
                            className="p-2 border rounded-md"
                        >
                            <option value={5}>5 / {t('page')}</option>
                            <option value={10}>10 / {t('page')}</option>
                            <option value={25}>25 / {t('page')}</option>
                        </select>
                    </div>
                </div>

                {/* Total */}
                <div className="text-sm text-gray-500 mt-2">
                    {t('total_users') || 'Total users'}: {filtered.length}
                </div>

                {/* Divider */}
                <hr className="border-gray-300"/>

                {/* User Cards */}
                <div className="space-y-6">
                    {paginated.map((user) => {
                        const isOpen = expanded === user.email;

                        return (
                            <div
                                key={user.email}
                                className="border rounded-lg shadow-sm bg-gray-50 hover:bg-gray-100 transition-all"
                            >
                                <div
                                    className="flex items-center justify-between p-4 cursor-pointer"
                                    onClick={() => setExpanded(isOpen ? null : user.email)}
                                >
                                    <div>
                                        <p className="font-semibold flex items-center gap-2">
                                            <Mail size={16}/> {user.email}
                                        </p>
                                        <p className="text-sm text-gray-600 flex items-center gap-1">
                                            <User size={14}/>
                                            {t('accessed_tags')}: {user.accessedTagsCount}
                                        </p>
                                    </div>
                                    {isOpen ? <ChevronUp size={20}/> : <ChevronDown size={20}/>}
                                </div>

                                {isOpen && (
                                    <div className="p-4 text-sm text-gray-700 space-y-2 border-t">
                                        <p className="flex items-center gap-2">
                                            <Calendar size={14}/>
                                            <span className="font-medium">{t('created_at')}:</span>{' '}
                                            {formatDate(user.createdAt)}
                                        </p>

                                        {user.userAccessedTagDtoList.length > 0 ? (
                                            <div>
                                                <p className="font-semibold mb-1">{t('tags')}:</p>
                                                <ul className="ml-4 list-disc space-y-1">
                                                    {user.userAccessedTagDtoList.map((tag, index) => (
                                                        <li key={index} className="text-gray-700">
                                                            <MapPin size={14} className="inline-block mr-1"/>
                                                            {tag.name} — {tag.latitude.toFixed(4)}, {tag.longitude.toFixed(4)} (
                                                            {formatDate(tag.unlockedAt)})
                                                        </li>
                                                    ))}
                                                </ul>
                                            </div>
                                        ) : (
                                            <p className="text-gray-500">{t('no_tags')}</p>
                                        )}
                                    </div>
                                )}
                            </div>
                        );
                    })}
                </div>

                {/* Pagination */}
                {totalPages > 1 && (
                    <div className="flex justify-center items-center gap-4 pt-4">
                        <button
                            onClick={() => setPage((p) => Math.max(p - 1, 1))}
                            disabled={page === 1}
                            className="p-2 border rounded-md hover:bg-gray-200 disabled:opacity-50"
                        >
                            <ChevronLeft size={16}/>
                        </button>
                        <span className="text-sm">
              {t('page')} {page} {t('of')} {totalPages}
            </span>
                        <button
                            onClick={() => setPage((p) => Math.min(p + 1, totalPages))}
                            disabled={page === totalPages}
                            className="p-2 border rounded-md hover:bg-gray-200 disabled:opacity-50"
                        >
                            <ChevronRight size={16}/>
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
}