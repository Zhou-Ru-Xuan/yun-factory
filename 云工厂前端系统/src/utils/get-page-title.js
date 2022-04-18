import defaultSettings from '@/settings'

const title = '智能云制造平台'

export default function getPageTitle(pageTitle) {
    if (pageTitle) {
        return `${pageTitle} - ${title}`
    }
    return `${title}`
}