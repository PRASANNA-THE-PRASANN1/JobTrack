import React, { useState, useEffect, useCallback } from 'react';
import Modal from '../common/Modal';
import StatusBadge from './StatusBadge';
import TimelineView from './TimelineView';
import ScheduleInterviewModal from './ScheduleInterviewModal';
import InterviewFeedbackModal from './InterviewFeedbackModal';
import DocumentModal from './DocumentModal';
import { applicationService } from '../../api/applicationService';
import { formatDate, formatDateTime, formatCurrency } from '../../utils/formatters';
import {
  ExternalLink,
  Calendar,
  MapPin,
  DollarSign,
  Mail,
  User,
  Briefcase,
  FileText,
  Edit3,
  Clock,
  Video,
  Star,
  Trash2,
  Plus,
  Paperclip,
  CheckCircle2,
  AlertCircle
} from 'lucide-react';

export default function ApplicationDetailModal({
  isOpen,
  onClose,
  application,
  onEdit,
  onStatusChange,
  initialTab = 'overview',
  onRefreshParent
}) {
  const [activeTab, setActiveTab] = useState(initialTab);
  const [interviews, setInterviews] = useState([]);
  const [timeline, setTimeline] = useState([]);
  const [documents, setDocuments] = useState([]);
  const [loadingInterviews, setLoadingInterviews] = useState(false);
  const [loadingTimeline, setLoadingTimeline] = useState(false);
  const [loadingDocs, setLoadingDocs] = useState(false);

  // Sub-modal states
  const [isScheduleOpen, setIsScheduleOpen] = useState(false);
  const [feedbackInterview, setFeedbackInterview] = useState(null);
  const [isDocModalOpen, setIsDocModalOpen] = useState(false);
  const [toastMessage, setToastMessage] = useState(null);

  const showToast = (msg, isError = false) => {
    setToastMessage({ text: msg, isError });
    setTimeout(() => setToastMessage(null), 3500);
  };

  const loadInterviews = useCallback(async () => {
    if (!application?.id) return;
    setLoadingInterviews(true);
    try {
      const res = await applicationService.getInterviews(application.id);
      if (res && res.success) {
        setInterviews(res.data || []);
      }
    } catch (err) {
      console.error('Failed to load interviews:', err);
    } finally {
      setLoadingInterviews(false);
    }
  }, [application?.id]);

  const loadTimeline = useCallback(async () => {
    if (!application?.id) return;
    setLoadingTimeline(true);
    try {
      const res = await applicationService.getTimeline(application.id);
      if (res && res.success) {
        setTimeline(res.data || []);
      }
    } catch (err) {
      console.error('Failed to load timeline:', err);
    } finally {
      setLoadingTimeline(false);
    }
  }, [application?.id]);

  const loadDocuments = useCallback(async () => {
    if (!application?.id) return;
    setLoadingDocs(true);
    try {
      const res = await applicationService.getDocuments(application.id);
      if (res && res.success) {
        setDocuments(res.data || []);
      }
    } catch (err) {
      console.error('Failed to load documents:', err);
    } finally {
      setLoadingDocs(false);
    }
  }, [application?.id]);

  useEffect(() => {
    if (isOpen && application?.id) {
      setActiveTab(initialTab);
      loadInterviews();
      loadTimeline();
      loadDocuments();
    }
  }, [isOpen, application?.id, initialTab, loadInterviews, loadTimeline, loadDocuments]);

  if (!application) return null;

  const statuses = ['SAVED', 'APPLIED', 'SCREENING', 'INTERVIEWING', 'OFFERED', 'REJECTED', 'WITHDRAWN'];

  // Handlers for Interviews
  const handleScheduleInterview = async (appId, formData) => {
    try {
      const res = await applicationService.scheduleInterview(appId, formData);
      showToast(res.message || 'Interview scheduled successfully');
      setIsScheduleOpen(false);
      loadInterviews();
      loadTimeline();
      onRefreshParent?.();
    } catch (err) {
      showToast(err.message || 'Failed to schedule interview', true);
    }
  };

  const handleSaveFeedback = async (interviewId, payload) => {
    try {
      const res = await applicationService.updateInterview(interviewId, payload);
      showToast(res.message || 'Interview updated successfully');
      setFeedbackInterview(null);
      loadInterviews();
      loadTimeline();
      onRefreshParent?.();
    } catch (err) {
      showToast(err.message || 'Failed to update interview', true);
    }
  };

  const handleDeleteInterview = async (interviewId) => {
    if (!window.confirm('Are you sure you want to delete this interview round?')) return;
    try {
      const res = await applicationService.deleteInterview(interviewId);
      showToast(res.message || 'Interview deleted');
      loadInterviews();
      loadTimeline();
      onRefreshParent?.();
    } catch (err) {
      showToast(err.message || 'Failed to delete interview', true);
    }
  };

  // Handlers for Timeline Notes
  const handleAddTimelineNote = async (payload) => {
    try {
      const res = await applicationService.addTimelineEvent(application.id, payload);
      showToast(res.message || 'Activity note logged');
      loadTimeline();
    } catch (err) {
      showToast(err.message || 'Failed to add activity note', true);
    }
  };

  // Handlers for Documents
  const handleAddDocument = async (appId, formData) => {
    try {
      const res = await applicationService.addDocument(appId, formData);
      showToast(res.message || 'Document attached successfully');
      setIsDocModalOpen(false);
      loadDocuments();
      loadTimeline();
    } catch (err) {
      showToast(err.message || 'Failed to attach document', true);
    }
  };

  const handleDeleteDocument = async (docId) => {
    if (!window.confirm('Are you sure you want to remove this document attachment?')) return;
    try {
      const res = await applicationService.deleteDocument(docId);
      showToast(res.message || 'Document removed');
      loadDocuments();
      loadTimeline();
    } catch (err) {
      showToast(err.message || 'Failed to delete document', true);
    }
  };

  const getInterviewStatusBadge = (status) => {
    switch (status) {
      case 'SCHEDULED':
        return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'COMPLETED':
        return 'bg-emerald-100 text-emerald-800 border-emerald-200';
      case 'RESCHEDULED':
        return 'bg-amber-100 text-amber-800 border-amber-200';
      case 'CANCELLED':
        return 'bg-rose-100 text-rose-800 border-rose-200';
      default:
        return 'bg-slate-100 text-slate-800 border-slate-200';
    }
  };

  const getDocumentTypeBadge = (type) => {
    switch (type) {
      case 'RESUME':
        return 'bg-indigo-50 text-indigo-700 border-indigo-200';
      case 'COVER_LETTER':
        return 'bg-sky-50 text-sky-700 border-sky-200';
      case 'OFFER_LETTER':
        return 'bg-emerald-50 text-emerald-700 border-emerald-200';
      case 'PORTFOLIO':
        return 'bg-purple-50 text-purple-700 border-purple-200';
      default:
        return 'bg-slate-50 text-slate-700 border-slate-200';
    }
  };

  return (
    <>
      <Modal isOpen={isOpen} onClose={onClose} title="Application Details" maxWidth="max-w-3xl">
        <div className="space-y-6">
          {/* Header summary */}
          <div className="flex flex-col sm:flex-row sm:items-center justify-between pb-4 border-b border-slate-100 gap-3">
            <div>
              <div className="flex items-center space-x-2">
                <h2 className="text-xl font-bold text-slate-900">{application.companyName}</h2>
                <StatusBadge status={application.status} size="sm" />
              </div>
              <p className="text-sm font-medium text-indigo-600 mt-0.5">{application.jobTitle}</p>
            </div>
            <div className="flex items-center space-x-2">
              <button
                onClick={() => {
                  onClose();
                  onEdit(application);
                }}
                className="inline-flex items-center space-x-1 px-3 py-1.5 text-xs font-medium text-slate-700 bg-slate-100 hover:bg-slate-200 rounded-lg transition-colors"
              >
                <Edit3 className="h-3.5 w-3.5 mr-1" />
                <span>Edit Application</span>
              </button>
            </div>
          </div>

          {/* Toast inside modal */}
          {toastMessage && (
            <div
              className={`p-3 rounded-lg text-xs font-medium border flex items-center space-x-2 ${
                toastMessage.isError
                  ? 'bg-rose-50 border-rose-200 text-rose-800'
                  : 'bg-emerald-50 border-emerald-200 text-emerald-800'
              }`}
            >
              {toastMessage.isError ? (
                <AlertCircle className="h-4 w-4 text-rose-600 flex-shrink-0" />
              ) : (
                <CheckCircle2 className="h-4 w-4 text-emerald-600 flex-shrink-0" />
              )}
              <span>{toastMessage.text}</span>
            </div>
          )}

          {/* Navigation Tabs */}
          <div className="flex items-center space-x-2 border-b border-slate-200 text-sm font-medium">
            <button
              onClick={() => setActiveTab('overview')}
              className={`pb-2.5 px-3 border-b-2 flex items-center space-x-1.5 transition-colors ${
                activeTab === 'overview'
                  ? 'border-indigo-600 text-indigo-600 font-semibold'
                  : 'border-transparent text-slate-500 hover:text-slate-800'
              }`}
            >
              <FileText className="h-4 w-4" />
              <span>Overview</span>
            </button>

            <button
              onClick={() => setActiveTab('interviews')}
              className={`pb-2.5 px-3 border-b-2 flex items-center space-x-1.5 transition-colors ${
                activeTab === 'interviews'
                  ? 'border-indigo-600 text-indigo-600 font-semibold'
                  : 'border-transparent text-slate-500 hover:text-slate-800'
              }`}
            >
              <Calendar className="h-4 w-4" />
              <span>Interviews</span>
              <span className="text-xs px-1.5 py-0.2 bg-slate-100 text-slate-600 rounded-full font-semibold">
                {interviews.length}
              </span>
            </button>

            <button
              onClick={() => setActiveTab('timeline')}
              className={`pb-2.5 px-3 border-b-2 flex items-center space-x-1.5 transition-colors ${
                activeTab === 'timeline'
                  ? 'border-indigo-600 text-indigo-600 font-semibold'
                  : 'border-transparent text-slate-500 hover:text-slate-800'
              }`}
            >
              <Clock className="h-4 w-4" />
              <span>Timeline</span>
              <span className="text-xs px-1.5 py-0.2 bg-slate-100 text-slate-600 rounded-full font-semibold">
                {timeline.length}
              </span>
            </button>

            <button
              onClick={() => setActiveTab('documents')}
              className={`pb-2.5 px-3 border-b-2 flex items-center space-x-1.5 transition-colors ${
                activeTab === 'documents'
                  ? 'border-indigo-600 text-indigo-600 font-semibold'
                  : 'border-transparent text-slate-500 hover:text-slate-800'
              }`}
            >
              <Paperclip className="h-4 w-4" />
              <span>Documents</span>
              <span className="text-xs px-1.5 py-0.2 bg-slate-100 text-slate-600 rounded-full font-semibold">
                {documents.length}
              </span>
            </button>
          </div>

          {/* TAB 1: OVERVIEW */}
          {activeTab === 'overview' && (
            <div className="space-y-6">
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
                        {application.salaryMax
                          ? ` – ${formatCurrency(application.salaryMax, application.salaryCurrency)}`
                          : '+'}
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
          )}

          {/* TAB 2: INTERVIEWS */}
          {activeTab === 'interviews' && (
            <div className="space-y-4">
              <div className="flex items-center justify-between pb-2 border-b border-slate-100">
                <div>
                  <h3 className="text-sm font-semibold text-slate-900">Scheduled & Completed Rounds</h3>
                  <p className="text-xs text-slate-500">Track each step of the interview process</p>
                </div>
                <button
                  type="button"
                  onClick={() => setIsScheduleOpen(true)}
                  className="inline-flex items-center space-x-1.5 px-3 py-1.5 text-xs font-medium text-white bg-indigo-600 hover:bg-indigo-700 rounded-lg shadow-sm transition-colors"
                >
                  <Plus className="h-3.5 w-3.5" />
                  <span>Schedule Round</span>
                </button>
              </div>

              {loadingInterviews ? (
                <div className="text-center py-8 text-xs text-slate-400">Loading interview rounds...</div>
              ) : interviews.length === 0 ? (
                <div className="text-center py-10 bg-slate-50 rounded-xl border border-dashed border-slate-200">
                  <Calendar className="h-8 w-8 text-slate-300 mx-auto mb-2" />
                  <p className="text-xs font-medium text-slate-600">No interview rounds scheduled yet.</p>
                  <p className="text-[11px] text-slate-400 mt-0.5">
                    Click "Schedule Round" to record your technical, screen, or loop interviews.
                  </p>
                </div>
              ) : (
                <div className="space-y-3">
                  {interviews.map((item) => (
                    <div
                      key={item.id}
                      className="p-4 bg-white rounded-xl border border-slate-200 shadow-xs hover:border-slate-300 transition-all space-y-3"
                    >
                      <div className="flex items-start justify-between">
                        <div>
                          <div className="flex items-center space-x-2">
                            <h4 className="text-sm font-bold text-slate-900">{item.roundName}</h4>
                            <span
                              className={`text-[10px] font-semibold px-2 py-0.5 rounded-full border ${getInterviewStatusBadge(
                                item.status
                              )}`}
                            >
                              {item.status}
                            </span>
                            <span className="text-[10px] font-medium px-2 py-0.5 rounded-full bg-slate-100 text-slate-700">
                              {item.roundType?.replace('_', ' ')}
                            </span>
                          </div>
                          <div className="flex flex-wrap items-center gap-3 text-xs text-slate-500 mt-1">
                            <span className="flex items-center space-x-1">
                              <Calendar className="h-3.5 w-3.5 text-slate-400" />
                              <span>{formatDateTime(item.interviewDate)}</span>
                            </span>
                            <span>•</span>
                            <span className="flex items-center space-x-1">
                              <Clock className="h-3.5 w-3.5 text-slate-400" />
                              <span>{item.durationMinutes} mins</span>
                            </span>
                            {item.interviewerNames && (
                              <>
                                <span>•</span>
                                <span className="flex items-center space-x-1">
                                  <User className="h-3.5 w-3.5 text-slate-400" />
                                  <span>{item.interviewerNames}</span>
                                </span>
                              </>
                            )}
                          </div>
                        </div>

                        <div className="flex items-center space-x-1.5">
                          <button
                            type="button"
                            onClick={() => setFeedbackInterview(item)}
                            className="px-2.5 py-1 text-xs font-medium text-indigo-700 bg-indigo-50 hover:bg-indigo-100 rounded-lg border border-indigo-200 transition-colors"
                          >
                            Feedback
                          </button>
                          <button
                            type="button"
                            onClick={() => handleDeleteInterview(item.id)}
                            className="p-1 text-slate-400 hover:text-rose-600 rounded-lg hover:bg-slate-100 transition-colors"
                            title="Delete interview"
                          >
                            <Trash2 className="h-4 w-4" />
                          </button>
                        </div>
                      </div>

                      {/* Meeting link */}
                      {item.meetingLink && (
                        <div className="flex items-center space-x-2 text-xs">
                          <Video className="h-3.5 w-3.5 text-emerald-600 flex-shrink-0" />
                          <a
                            href={item.meetingLink}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="text-emerald-700 font-medium hover:underline break-all"
                          >
                            {item.meetingLink}
                          </a>
                        </div>
                      )}

                      {/* Feedback & Rating display */}
                      {(item.rating || item.feedbackNotes) && (
                        <div className="p-3 bg-slate-50 rounded-lg border border-slate-200 text-xs text-slate-700 space-y-1">
                          {item.rating && (
                            <div className="flex items-center space-x-1 text-amber-500">
                              {[1, 2, 3, 4, 5].map((star) => (
                                <Star
                                  key={star}
                                  className={`h-3.5 w-3.5 ${
                                    star <= item.rating ? 'fill-amber-400 text-amber-400' : 'text-slate-300'
                                  }`}
                                />
                              ))}
                              <span className="text-[11px] font-semibold text-slate-600 ml-1">
                                Rating: {item.rating}/5
                              </span>
                            </div>
                          )}
                          {item.feedbackNotes && (
                            <p className="whitespace-pre-wrap leading-relaxed text-slate-600">{item.feedbackNotes}</p>
                          )}
                        </div>
                      )}
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}

          {/* TAB 3: TIMELINE */}
          {activeTab === 'timeline' && (
            <div>
              <div className="mb-4">
                <h3 className="text-sm font-semibold text-slate-900">Application Activity & Audit Log</h3>
                <p className="text-xs text-slate-500">
                  Automatically records status transitions, interview updates, and manual notes.
                </p>
              </div>
              <TimelineView
                events={timeline}
                onAddNote={handleAddTimelineNote}
                loading={loadingTimeline}
              />
            </div>
          )}

          {/* TAB 4: DOCUMENTS */}
          {activeTab === 'documents' && (
            <div className="space-y-4">
              <div className="flex items-center justify-between pb-2 border-b border-slate-100">
                <div>
                  <h3 className="text-sm font-semibold text-slate-900">Attached Documents & Artifacts</h3>
                  <p className="text-xs text-slate-500">Attach tailored resumes, cover letters, portfolios, or offers</p>
                </div>
                <button
                  type="button"
                  onClick={() => setIsDocModalOpen(true)}
                  className="inline-flex items-center space-x-1.5 px-3 py-1.5 text-xs font-medium text-white bg-indigo-600 hover:bg-indigo-700 rounded-lg shadow-sm transition-colors"
                >
                  <Plus className="h-3.5 w-3.5" />
                  <span>Attach Document</span>
                </button>
              </div>

              {loadingDocs ? (
                <div className="text-center py-8 text-xs text-slate-400">Loading documents...</div>
              ) : documents.length === 0 ? (
                <div className="text-center py-10 bg-slate-50 rounded-xl border border-dashed border-slate-200">
                  <Paperclip className="h-8 w-8 text-slate-300 mx-auto mb-2" />
                  <p className="text-xs font-medium text-slate-600">No documents attached to this application.</p>
                  <p className="text-[11px] text-slate-400 mt-0.5">
                    Click "Attach Document" to add resume links, portfolio references, or offer letters.
                  </p>
                </div>
              ) : (
                <div className="space-y-2.5">
                  {documents.map((doc) => (
                    <div
                      key={doc.id}
                      className="p-3 bg-white rounded-xl border border-slate-200 shadow-xs flex items-center justify-between hover:border-slate-300 transition-all"
                    >
                      <div className="flex items-start space-x-3">
                        <div className="p-2 bg-indigo-50 rounded-lg text-indigo-600 mt-0.5">
                          <Paperclip className="h-4 w-4" />
                        </div>
                        <div>
                          <div className="flex items-center space-x-2">
                            <h4 className="text-sm font-semibold text-slate-900">{doc.documentName}</h4>
                            <span
                              className={`text-[10px] font-semibold px-2 py-0.5 rounded-full border ${getDocumentTypeBadge(
                                doc.documentType
                              )}`}
                            >
                              {doc.documentType?.replace('_', ' ')}
                            </span>
                          </div>
                          {doc.notes && <p className="text-xs text-slate-500 mt-0.5">{doc.notes}</p>}
                          {doc.documentUrl && (
                            <a
                              href={doc.documentUrl}
                              target="_blank"
                              rel="noopener noreferrer"
                              className="inline-flex items-center space-x-1 text-xs text-indigo-600 hover:underline mt-1"
                            >
                              <span>{doc.documentUrl}</span>
                              <ExternalLink className="h-3 w-3" />
                            </a>
                          )}
                        </div>
                      </div>

                      <button
                        type="button"
                        onClick={() => handleDeleteDocument(doc.id)}
                        className="p-1.5 text-slate-400 hover:text-rose-600 rounded-lg hover:bg-slate-100 transition-colors ml-3"
                        title="Remove document"
                      >
                        <Trash2 className="h-4 w-4" />
                      </button>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}
        </div>
      </Modal>

      {/* Embedded Sub-Modals */}
      <ScheduleInterviewModal
        isOpen={isScheduleOpen}
        onClose={() => setIsScheduleOpen(false)}
        onSchedule={handleScheduleInterview}
        applicationId={application.id}
        companyName={application.companyName}
      />

      <InterviewFeedbackModal
        isOpen={!!feedbackInterview}
        onClose={() => setFeedbackInterview(null)}
        interview={feedbackInterview}
        onSaveFeedback={handleSaveFeedback}
      />

      <DocumentModal
        isOpen={isDocModalOpen}
        onClose={() => setIsDocModalOpen(false)}
        onAddDocument={handleAddDocument}
        applicationId={application.id}
      />
    </>
  );
}
