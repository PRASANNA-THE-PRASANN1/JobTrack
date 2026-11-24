import React, { useState } from 'react';
import Modal from '../common/Modal';

export default function ScheduleInterviewModal({ isOpen, onClose, onSchedule, applicationId, companyName }) {
  const [formData, setFormData] = useState({
    roundName: '',
    roundType: 'TECHNICAL',
    interviewDate: '',
    durationMinutes: 45,
    interviewerNames: '',
    meetingLink: '',
    feedbackNotes: ''
  });

  const [errors, setErrors] = useState({});

  const validate = () => {
    const errs = {};
    if (!formData.roundName.trim()) errs.roundName = 'Round name is required';
    if (!formData.interviewDate) errs.interviewDate = 'Date and time are required';
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors((prev) => ({ ...prev, [name]: null }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return;
    onSchedule(applicationId, formData);
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={`Schedule Interview — ${companyName}`} maxWidth="max-w-xl">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
            Round Name <span className="text-rose-500">*</span>
          </label>
          <input
            type="text"
            name="roundName"
            value={formData.roundName}
            onChange={handleChange}
            placeholder="e.g. Technical Screen, System Design, Final Onsite"
            className={`w-full px-3 py-2 text-sm rounded-lg border ${
              errors.roundName ? 'border-rose-400 bg-rose-50/40' : 'border-slate-300'
            } focus:outline-none focus:ring-2 focus:ring-indigo-500`}
          />
          {errors.roundName && <p className="text-xs text-rose-500 mt-1">{errors.roundName}</p>}
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
              Round Type <span className="text-rose-500">*</span>
            </label>
            <select
              name="roundType"
              value={formData.roundType}
              onChange={handleChange}
              className="w-full px-3 py-2 text-sm rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 bg-white"
            >
              <option value="PHONE_SCREEN">Phone Screen</option>
              <option value="TECHNICAL">Technical Coding</option>
              <option value="SYSTEM_DESIGN">System Design</option>
              <option value="BEHAVIORAL">Behavioral</option>
              <option value="TAKE_HOME">Take Home Review</option>
              <option value="ONSITE">Onsite Loop</option>
              <option value="FINAL_ROUND">Final Round</option>
            </select>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
              Duration (Minutes)
            </label>
            <select
              name="durationMinutes"
              value={formData.durationMinutes}
              onChange={handleChange}
              className="w-full px-3 py-2 text-sm rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 bg-white"
            >
              <option value={30}>30 minutes</option>
              <option value={45}>45 minutes</option>
              <option value={60}>60 minutes</option>
              <option value={90}>90 minutes</option>
              <option value={120}>2 hours</option>
            </select>
          </div>
        </div>

        <div>
          <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
            Date & Time <span className="text-rose-500">*</span>
          </label>
          <input
            type="datetime-local"
            name="interviewDate"
            value={formData.interviewDate}
            onChange={handleChange}
            className={`w-full px-3 py-2 text-sm rounded-lg border ${
              errors.interviewDate ? 'border-rose-400 bg-rose-50/40' : 'border-slate-300'
            } focus:outline-none focus:ring-2 focus:ring-indigo-500`}
          />
          {errors.interviewDate && <p className="text-xs text-rose-500 mt-1">{errors.interviewDate}</p>}
        </div>

        <div>
          <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
            Interviewer Name(s)
          </label>
          <input
            type="text"
            name="interviewerNames"
            value={formData.interviewerNames}
            onChange={handleChange}
            placeholder="e.g. John Smith (Staff Engineer), Lisa Ray (HM)"
            className="w-full px-3 py-2 text-sm rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
        </div>

        <div>
          <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
            Meeting Link (Zoom / Google Meet / Teams)
          </label>
          <input
            type="url"
            name="meetingLink"
            value={formData.meetingLink}
            onChange={handleChange}
            placeholder="https://meet.google.com/xyz-abc"
            className="w-full px-3 py-2 text-sm rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
        </div>

        <div>
          <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
            Preparation Notes
          </label>
          <textarea
            name="feedbackNotes"
            rows="2"
            value={formData.feedbackNotes}
            onChange={handleChange}
            placeholder="Topics to prepare, questions to ask, system components to review..."
            className="w-full px-3 py-2 text-sm rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
        </div>

        <div className="flex items-center justify-end space-x-3 pt-4 border-t border-slate-100">
          <button
            type="button"
            onClick={onClose}
            className="px-4 py-2 text-sm font-medium text-slate-700 bg-white border border-slate-300 rounded-lg hover:bg-slate-50 transition-colors"
          >
            Cancel
          </button>
          <button
            type="submit"
            className="px-5 py-2 text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 rounded-lg shadow-sm hover:shadow transition-all"
          >
            Confirm & Schedule
          </button>
        </div>
      </form>
    </Modal>
  );
}
