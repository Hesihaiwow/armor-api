import moment from 'moment'
export const _formatDate = (value) => {
    return moment(value).format('YYYY-MM-DD');
}
export const _formatTime = (value) => {
    return moment(value).format('HH:mm:ss');
}
export const _formatDateTime = (value) => {
    return moment(value).format('YYYY-MM-DD HH:mm');
}
export const _dateFormat = (value, pattern) => {
    return moment(value).format(pattern);
}