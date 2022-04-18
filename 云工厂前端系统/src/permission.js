import router, { adminRoutes, factoryRoutes, dealerRoutes, endRoutes } from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'


NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/register'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
    // start progress bar
    NProgress.start()

    // set page title
    document.title = getPageTitle(to.meta.title)

    // determine whether the user has logged in
    const hasToken = getToken()



    if (hasToken) {
        if (to.path === '/login') {
            // if is logged in, redirect to the home page
            next({ path: '/' })
            NProgress.done()
        } else {
            const hasGetUserInfo = store.getters.name;
            if (hasGetUserInfo) {
                next()
            } else {
                try {
                    // get user info
                    await store.dispatch('user/getInfo')
                    const userRole = store.getters.role;
                    console.log(userRole);
                    // debugger
                    let addRoutes;
                    if (userRole === '超级管理员') {
                        addRoutes = [...adminRoutes, ...endRoutes];
                    } else if (userRole === '云工厂') {
                        addRoutes = [...factoryRoutes, ...endRoutes]
                    } else {
                        addRoutes = [...dealerRoutes, ...endRoutes]
                    }
                    console.log(addRoutes)

                    router.addRoutes(addRoutes);
                    router.options.routes = router.options.routes.concat(addRoutes); //把侧边栏的路由显示更改，还要去路由表中的resetRouter中将退出登录时的路由删除
                    // debugger
                    // next()
                    //加个判断，解决刷新路由丢失的问题
                    if (to.matched.length === 0) next({ path: to.path })
                    else next()
                } catch (error) {
                    // remove token and go to login page to re-login
                    await store.dispatch('user/resetToken')
                        // Message.error(error || 'Has Error')
                    next(`/login?redirect=${to.path}`)
                    NProgress.done()
                }
            }
        }
    } else {
        /* has no token*/

        if (whiteList.indexOf(to.path) !== -1) {
            // in the free login whitelist, go directly
            next()
        } else {
            // other pages that do not have permission to access are redirected to the login page.
            next(`/login?redirect=${to.path}`)
            NProgress.done()
        }
    }
})

router.afterEach(() => {
    // finish progress bar
    NProgress.done()
})