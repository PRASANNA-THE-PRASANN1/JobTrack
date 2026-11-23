import React from 'react';
import { Eye, Edit3, Trash2, ExternalLink, MapPin, Calendar, DollarSign, Briefcase } from 'lucide-react';
import StatusBadge from './StatusBadge';
import { formatDate, formatCurrency } from '../../utils/formatters';

export default function ApplicationList({
  applications,
  loading,
  onView,
  onEdit,
  onDelete,
  page,
  totalPages,
  totalElements,
  onPageChange
}) {
  if (loading && (!applications || applications.length === 0)) {
    return (
      <div className="bg-white rounded-xl border border-slate-200 p-12 text-center shadow-sm">
        <div className="inline-block animate-spin rounded-full h-8 w-8 border-4 border-indigo-600 border-t-transparent mb-3" />
        <p className="text-sm font-medium text-slate-600">Loading your applications...</p>
      </div>
    );
  }

  if (!applications || applications.length === 0) {
    return (
      <div className="bg-white rounded-xl border border-slate-200 p-12 text-center shadow-sm">
        <div className="mx-auto h-12 w-12 rounded-full bg-slate-100 flex items-center justify-center text-slate-400 mb-4">
          <Briefcase className="h-6 w-6" />
        </div>
        <h3 className="text-base font-semibold text-slate-900 mb-1">No applications found</h3>
        <p className="text-sm text-slate-500 max-w-sm mx-auto mb-4">
          No job applications match your current filters. Add a new application or adjust your search criteria.
        </p>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
      {/* Table view for md+ screens */}
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-slate-200 text-left text-sm">
          <thead className="bg-slate-50/75 text-xs uppercase font-semibold text-slate-500 tracking-wider">
            <tr>
              <th scope="col" className="px-6 py-3.5">Company & Role</th>
              <th scope="col" className="px-6 py-3.5">Status</th>
              <th scope="col" className="px-6 py-3.5">Work Mode</th>
              <th scope="col" className="px-6 py-3.5">Salary</th>
              <th scope="col" className="px-6 py-3.5">Date Applied</th>
              <th scope="col" className="px-6 py-3.5 text-right">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100 bg-white">
            {applications.map((app) => (
              <tr key={app.id} className="hover:bg-slate-50/75 transition-colors group">
                <td className="px-6 py-4">
                  <div className="flex items-start justify-between">
                    <div>
                      <div className="font-semibold text-slate-900 group-hover:text-indigo-600 transition-colors">
                        {app.companyName}
                      </div>
                      <div className="text-xs text-slate-500 font-medium mt-0.5">{app.jobTitle}</div>
                      {app.jobLocation && (
                        <div className="flex items-center text-[11px] text-slate-400 mt-1">
                          <MapPin className="h-3 w-3 mr-1" />
                          <span>{app.jobLocation}</span>
                        </div>
                      )}
                    </div>
                    {app.jobUrl && (
                      <a
                        href={app.jobUrl}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-slate-400 hover:text-indigo-600 p-1"
                        title="Open posting"
                      >
                        <ExternalLink className="h-3.5 w-3.5" />
                      </a>
                    )}
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <StatusBadge status={app.status} />
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className="inline-flex items-center text-xs font-medium text-slate-600 bg-slate-100 px-2.5 py-1 rounded-md">
                    {app.workMode?.replace('_', ' ')}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-xs text-slate-700">
                  {app.salaryMin || app.salaryMax ? (
                    <span className="font-medium text-slate-800">
                      {formatCurrency(app.salaryMin, app.salaryCurrency)}
                      {app.salaryMax ? ` – ${formatCurrency(app.salaryMax, app.salaryCurrency)}` : '+'}
                    </span>
                  ) : (
                    <span className="text-slate-400">—</span>
                  )}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-xs text-slate-600">
                  <div className="flex items-center space-x-1.5">
                    <Calendar className="h-3.5 w-3.5 text-slate-400" />
                    <span>{formatDate(app.applicationDate)}</span>
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right">
                  <div className="flex items-center justify-end space-x-1">
                    <button
                      onClick={() => onView(app)}
                      title="View Details"
                      className="p-1.5 text-slate-400 hover:text-indigo-600 hover:bg-indigo-50 rounded-lg transition-colors"
                    >
                      <Eye className="h-4 w-4" />
                    </button>
                    <button
                      onClick={() => onEdit(app)}
                      title="Edit"
                      className="p-1.5 text-slate-400 hover:text-amber-600 hover:bg-amber-50 rounded-lg transition-colors"
                    >
                      <Edit3 className="h-4 w-4" />
                    </button>
                    <button
                      onClick={() => onDelete(app)}
                      title="Delete"
                      className="p-1.5 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-lg transition-colors"
                    >
                      <Trash2 className="h-4 w-4" />
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Pagination Footer */}
      {totalPages > 1 && (
        <div className="px-6 py-3.5 border-t border-slate-200 bg-slate-50/50 flex items-center justify-between">
          <p className="text-xs text-slate-500">
            Showing <span className="font-medium text-slate-700">{applications.length}</span> of{' '}
            <span className="font-medium text-slate-700">{totalElements}</span> applications
          </p>
          <div className="flex items-center space-x-2">
            <button
              onClick={() => onPageChange(page - 1)}
              disabled={page === 0}
              className="px-3 py-1.5 text-xs font-medium text-slate-700 bg-white border border-slate-300 rounded-lg hover:bg-slate-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
            >
              Previous
            </button>
            <span className="text-xs text-slate-600 font-medium px-2">
              Page {page + 1} of {totalPages}
            </span>
            <button
              onClick={() => onPageChange(page + 1)}
              disabled={page >= totalPages - 1}
              className="px-3 py-1.5 text-xs font-medium text-slate-700 bg-white border border-slate-300 rounded-lg hover:bg-slate-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
            >
              Next
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
