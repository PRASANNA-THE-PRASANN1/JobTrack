import api from './client';

export const applicationService = {
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
  }
};
