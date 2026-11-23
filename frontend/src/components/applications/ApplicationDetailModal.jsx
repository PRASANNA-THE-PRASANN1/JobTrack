import React from 'react';
import Modal from '../common/Modal';
import StatusBadge from './StatusBadge';
import { formatDate, formatCurrency } from '../../utils/formatters';
import { ExternalLink, Calendar, MapPin, DollarSign, Mail, User, Briefcase, FileText, Edit3 } from 'lucide-react';

export default function ApplicationDetailModal({ isOpen, onClose, application, onEdit, onStatusChange }) {
  if (!application) return null;

  const statuses = ['SAVED', 'APPLIED', 'SCREENING', 'INTERVIEWING', 'OFFERED', 'REJECTED', 'WITHDRAWN'];

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Application Details" maxWidth="max-w-2xl">
      <div className="space-y-6">
        {/* Header summary */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between pb-4 border-b border-slate-100 gap-3">
          <div>
            <h2 className="text-xl font-bold text-slate-900">{application.companyName}</h2>
            <p className="text-sm font-medium text-indigo-600 mt-0.5">{application.jobTitle}</p>
          </div>
          <div className="flex items-center space-x-3">
            <StatusBadge status={application.status} size="md" />
            <button
              onClick={() => {
                onClose();
                onEdit(application);
              }}
              className="inline-flex items-center space-x-1 px-3 py-1.5 text-xs font-medium text-slate-700 bg-slate-100 hover:bg-slate-200 rounded-lg transition-colors"
            >
              <Edit3 className="h-3.5 w-3.5 mr-1" />
              <span>Edit</span>
            </button>
          </div>
        </div>

        {/* Status quick switcher */}
        <div className="bg-slate-50 p-3 rounded-xl border border-slate-200">
          <label className="block text-[11px] font-semibold text-slate-500 uppercase tracking-wider mb-2">
            Quick Status Update
          </label>
          <div className="flex flex-wrap gap-1.5">
            {statuses.map((st) => (
              <button
                key={st}
                onClick={() => onStatusChange(application.id, st)}
                className={`text-xs px-2.5 py-1 rounded-lg border transition-all ${
                  application.status === st
                    ? 'bg-indigo-600 text-white border-indigo-600 font-semibold shadow-sm'
                    : 'bg-white text-slate-700 border-slate-200 hover:bg-slate-100'
                }`}
              >
                {st.replace('_', ' ')}
              </button>
            ))}
          </div>
        </div>

        {/* Details grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-sm">
          <div className="flex items-start space-x-3 text-slate-700">
            <MapPin className="h-4 w-4 text-slate-400 mt-0.5 flex-shrink-0" />
            <div>
              <span className="block text-xs font-semibold text-slate-500 uppercase">Location & Mode</span>
              <span>{application.jobLocation || 'Not specified'}</span>
              <span className="text-xs text-slate-400 ml-1.5 font-medium">({application.workMode})</span>
            </div>
          </div>

          <div className="flex items-start space-x-3 text-slate-700">
            <Calendar className="h-4 w-4 text-slate-400 mt-0.5 flex-shrink-0" />
            <div>
              <span className="block text-xs font-semibold text-slate-500 uppercase">Applied On</span>
              <span>{formatDate(application.applicationDate)}</span>
            </div>
          </div>

          <div className="flex items-start space-x-3 text-slate-700">
            <DollarSign className="h-4 w-4 text-slate-400 mt-0.5 flex-shrink-0" />
            <div>
              <span className="block text-xs font-semibold text-slate-500 uppercase">Salary Range</span>
              {application.salaryMin || application.salaryMax ? (
                <span>
                  {formatCurrency(application.salaryMin, application.salaryCurrency)}
                  {application.salaryMax ? ` – ${formatCurrency(application.salaryMax, application.salaryCurrency)}` : '+'}
                </span>
              ) : (
                <span className="text-slate-400">Not specified</span>
              )}
            </div>
          </div>

          <div className="flex items-start space-x-3 text-slate-700">
            <Briefcase className="h-4 w-4 text-slate-400 mt-0.5 flex-shrink-0" />
            <div>
              <span className="block text-xs font-semibold text-slate-500 uppercase">Employment Type</span>
              <span>{application.employmentType?.replace('_', ' ')}</span>
            </div>
          </div>
        </div>

        {/* Recruiter contact */}
        {(application.contactPerson || application.contactEmail) && (
          <div className="p-3 bg-slate-50 rounded-xl border border-slate-200">
            <span className="block text-xs font-semibold text-slate-500 uppercase tracking-wider mb-2">
              Recruiter / Contact Person
            </span>
            <div className="flex flex-wrap items-center gap-4 text-sm text-slate-700">
              {application.contactPerson && (
                <div className="flex items-center space-x-1.5">
                  <User className="h-4 w-4 text-slate-400" />
                  <span className="font-medium">{application.contactPerson}</span>
                </div>
              )}
              {application.contactEmail && (
                <div className="flex items-center space-x-1.5">
                  <Mail className="h-4 w-4 text-slate-400" />
                  <a href={`mailto:${application.contactEmail}`} className="text-indigo-600 hover:underline">
                    {application.contactEmail}
                  </a>
                </div>
              )}
            </div>
          </div>
        )}

        {/* Job URL */}
        {application.jobUrl && (
          <div>
            <span className="block text-xs font-semibold text-slate-500 uppercase tracking-wider mb-1">
              Job Listing
            </span>
            <a
              href={application.jobUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="inline-flex items-center space-x-1.5 text-sm text-indigo-600 hover:text-indigo-800 hover:underline break-all"
            >
              <span>{application.jobUrl}</span>
              <ExternalLink className="h-3.5 w-3.5 flex-shrink-0" />
            </a>
          </div>
        )}

        {/* Notes */}
        {application.notes && (
          <div>
            <span className="block text-xs font-semibold text-slate-500 uppercase tracking-wider mb-1.5">
              Notes & Follow-ups
            </span>
            <div className="p-3.5 bg-amber-50/60 border border-amber-200/80 rounded-xl text-sm text-slate-800 whitespace-pre-wrap font-sans">
              {application.notes}
            </div>
          </div>
        )}

        {/* Job Description */}
        {application.jobDescription && (
          <div>
            <span className="block text-xs font-semibold text-slate-500 uppercase tracking-wider mb-1.5">
              Job Description
            </span>
            <div className="p-3.5 bg-slate-50 border border-slate-200 rounded-xl text-xs text-slate-700 whitespace-pre-wrap max-h-48 overflow-y-auto font-mono">
              {application.jobDescription}
            </div>
          </div>
        )}
      </div>
    </Modal>
  );
}
