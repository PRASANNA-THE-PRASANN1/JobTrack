import api from './client';

export const applicationService = {
  // Phase 1 - Applications CRUD
  getApplications: (params = {}) => {
    return api.get('/applications', { params });
  },

  getApplicationById: (id) => {
    return api.get(`/applications/${id}`);
  },

  createApplication: (payload) => {
    return api.post('/applications', payload);
  },

  updateApplication: (id, payload) => {
    return api.put(`/applications/${id}`, payload);
  },

  deleteApplication: (id) => {
    return api.delete(`/applications/${id}`);
  },

  getStats: () => {
    return api.get('/applications/stats');
  },

  checkHealth: () => {
    return api.get('/health');
  },

  // Phase 2 - Interviews
  getInterviews: (appId) => {
    return api.get(`/applications/${appId}/interviews`);
  },

  getUpcomingInterviews: () => {
    return api.get('/interviews/upcoming');
  },

  scheduleInterview: (appId, payload) => {
    return api.post(`/applications/${appId}/interviews`, payload);
  },

  updateInterview: (id, payload) => {
    return api.put(`/interviews/${id}`, payload);
  },

  deleteInterview: (id) => {
    return api.delete(`/interviews/${id}`);
  },

  // Phase 2 - Timeline
  getTimeline: (appId) => {
    return api.get(`/applications/${appId}/timeline`);
  },

  addTimelineEvent: (appId, payload) => {
    return api.post(`/applications/${appId}/timeline`, payload);
  },

  // Phase 2 - Documents
  getDocuments: (appId) => {
    return api.get(`/applications/${appId}/documents`);
  },

  addDocument: (appId, payload) => {
    return api.post(`/applications/${appId}/documents`, payload);
  },

  deleteDocument: (id) => {
    return api.delete(`/documents/${id}`);
  }
};
