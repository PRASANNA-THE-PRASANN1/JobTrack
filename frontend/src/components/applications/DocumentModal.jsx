import React, { useState } from 'react';
import Modal from '../common/Modal';

export default function DocumentModal({ isOpen, onClose, onAddDocument, applicationId }) {
  const [formData, setFormData] = useState({
    documentName: '',
    documentType: 'RESUME',
    documentUrl: '',
    notes: ''
  });

  const [errors, setErrors] = useState({});

  const validate = () => {
    const errs = {};
    if (!formData.documentName.trim()) errs.documentName = 'Document name is required';
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
    onAddDocument(applicationId, formData);
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Attach Document or Link" maxWidth="max-w-md">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
            Document Title <span className="text-rose-500">*</span>
          </label>
          <input
            type="text"
            name="documentName"
            value={formData.documentName}
            onChange={handleChange}
            placeholder="e.g. Resume_Backend_v2.pdf, Portfolio Link"
            className={`w-full px-3 py-2 text-sm rounded-lg border ${
              errors.documentName ? 'border-rose-400 bg-rose-50/40' : 'border-slate-300'
            } focus:outline-none focus:ring-2 focus:ring-indigo-500`}
          />
          {errors.documentName && <p className="text-xs text-rose-500 mt-1">{errors.documentName}</p>}
        </div>

        <div>
          <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
            Type <span className="text-rose-500">*</span>
          </label>
          <select
            name="documentType"
            value={formData.documentType}
            onChange={handleChange}
            className="w-full px-3 py-2 text-sm rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 bg-white"
          >
            <option value="RESUME">Resume / CV</option>
            <option value="COVER_LETTER">Cover Letter</option>
            <option value="PORTFOLIO">Portfolio / GitHub Link</option>
            <option value="OFFER_LETTER">Offer Letter</option>
            <option value="TRANSCRIPT">Academic Transcript</option>
            <option value="OTHER">Other</option>
          </select>
        </div>

        <div>
          <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
            Document / Resource URL
          </label>
          <input
            type="url"
            name="documentUrl"
            value={formData.documentUrl}
            onChange={handleChange}
            placeholder="https://drive.google.com/... or https://github.com/..."
            className="w-full px-3 py-2 text-sm rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
        </div>

        <div>
          <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
            Notes
          </label>
          <input
            type="text"
            name="notes"
            value={formData.notes}
            onChange={handleChange}
            placeholder="e.g. Customized for cloud infrastructure requirements"
            className="w-full px-3 py-2 text-sm rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
        </div>

        <div className="flex items-center justify-end space-x-3 pt-3 border-t border-slate-100">
          <button
            type="button"
            onClick={onClose}
            className="px-4 py-2 text-sm font-medium text-slate-700 bg-white border border-slate-300 rounded-lg hover:bg-slate-50"
          >
            Cancel
          </button>
          <button
            type="submit"
            className="px-5 py-2 text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 rounded-lg shadow-sm"
          >
            Attach Document
          </button>
        </div>
      </form>
    </Modal>
  );
}
