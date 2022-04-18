import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */

// export const constantRoutes = [


//   ]
// export const constantRoutes = [


// ]
export const constantRoutes = [
    // 首页
    {
        path: '/',
        component: Layout,
        redirect: '/dashboard',

        children: [{
            path: 'dashboard',
            name: 'dashboard',
            component: () =>
                import ('@/views/dashboard/index'),
            meta: {
                title: '首页',
                icon: 'el-icon-s-home',
            }
        }]
    }, {
        path: '/login',
        component: () =>
            import ('@/views/login/index'),
        hidden: true
    },
    {
        path: '/register',
        component: () =>
            import ('@/views/cloudfactory/user/register'),
        hidden: true
    },








]
export const adminRoutes = [{
        path: '/acl',
        component: Layout,
        redirect: '/acl/user/list',
        name: '权限管理',
        meta: {
            title: '权限管理',
            icon: 'el-icon-s-order',
            role: ['超级管理员']
        },
        children: [
            // {
            //     path: 'user/list',
            //     name: '用户管理',
            //     component: () =>
            //         import ('@/views/cloudfactory/acl/user/list'),
            //     meta: {
            //         title: '用户管理',
            //         role: ['超级管理员']
            //     }
            // },
            {
                path: 'role/list',
                name: '角色管理',
                component: () =>
                    import ('@/views/cloudfactory/acl/role/list'),
                meta: {
                    title: '角色管理',
                    icon: 'el-icon-user-solid',
                    role: ['超级管理员']
                }
            },
            {
                path: 'role/form',
                name: '角色添加',
                component: () =>
                    import ('@/views/cloudfactory/acl/role/form'),
                meta: {
                    title: '角色添加',
                    role: ['超级管理员']
                },
                hidden: true
            },
            {
                path: 'role/update/:id',
                name: '角色修改',
                component: () =>
                    import ('@/views/cloudfactory/acl/role/form'),
                meta: {
                    title: '角色修改',
                    role: ['超级管理员']
                },
                hidden: true
            },
            {
                path: 'role/distribution/:id',
                name: '角色权限',
                component: () =>
                    import ('@/views/cloudfactory/acl/role/roleForm'),
                meta: {
                    title: '角色权限',
                    role: ['超级管理员']
                },
                hidden: true
            },
            {
                path: 'menu/list',
                name: '菜单管理',
                component: () =>
                    import ('@/views/cloudfactory/acl/menu/list'),
                meta: {
                    title: '菜单管理',
                    icon: 'el-icon-menu',
                    role: ['超级管理员']
                }
            },
            // {
            //     path: 'user/add',
            //     name: '用户添加',
            //     component: () =>
            //         import ('@/views/cloudfactory/acl/user/form'),
            //     meta: {
            //         title: '用户添加',
            //         role: ['超级管理员']
            //     },
            //     hidden: true
            // },
            // {
            //     path: 'user/update/:id',
            //     name: '用户修改',
            //     component: () =>
            //         import ('@/views/cloudfactory/acl/user/form'),
            //     meta: {
            //         title: '用户修改',
            //         role: ['超级管理员']
            //     },
            //     hidden: true
            // },
            // {
            //     path: 'user/role/:id',
            //     name: '用户角色',
            //     component: () =>
            //         import ('@/views/cloudfactory/acl/user/roleForm'),
            //     meta: {
            //         title: '用户角色',
            //         role: ['超级管理员']
            //     },
            //     hidden: true
            // }

        ]
    },
    {
        path: '/user',
        component: Layout,
        // 设置/user的默认值
        redirect: '/user/user-info',
        name: '用户管理',
        meta: {
            title: '用户管理',
            icon: 'el-icon-user',
            role: ['超级管理员']
        },
        children: [{
                path: 'user-info',
                name: '用户列表',
                component: () =>
                    import ('@/views/cloudfactory/user/user-info'),
                meta: {
                    title: '用户列表',
                    icon: 'el-icon-bank-card',
                    role: ['超级管理员']
                }
            },
            {
                path: 'user-add',
                name: '添加用户',
                component: () =>
                    import ('@/views/cloudfactory/user/user-add'),
                meta: {
                    title: '添加用户',

                    role: ['超级管理员']
                },
                hidden: true
            },

            {
                path: 'edit/:id', //:id是占位符   
                name: '编辑用户',
                component: () =>
                    import ('@/views/cloudfactory/user/user-add'),
                meta: {
                    title: '编辑用户',
                    noCache: true,
                    role: ['超级管理员']
                },
                hidden: true
            },
            {
                path: 'factory-info',
                name: '云工厂信息',
                component: () =>
                    import ('@/views/cloudfactory/user/factory-info'),
                meta: {
                    title: '云工厂信息',
                    icon: 'el-icon-discount',
                    role: ['超级管理员']
                }
            }
        ]
    },
    {
        path: '/product',
        component: Layout,
        // 设置/user的默认值
        redirect: '/product/product-info',
        name: '产品管理',
        meta: {
            title: '产品管理',
            icon: 'el-icon-mobile-phone',
            role: ['超级管理员']
        },
        children: [{
                path: 'product-info',
                name: '产品信息管理',
                component: () =>
                    import ('@/views/cloudfactory/product/product-info'),
                meta: {
                    title: '产品信息管理',
                    icon: 'el-icon-watch',
                    role: ['超级管理员']
                }
            },
            {
                path: 'product-add',
                name: '添加产品',
                component: () =>
                    import ('@/views/cloudfactory/product/product-add'),
                meta: {
                    title: '添加产品',

                    role: ['超级管理员']
                },
                hidden: true
            },

            {
                path: 'product-edit/:id', //:id是占位符   
                name: '编辑产品',
                component: () =>
                    import ('@/views/cloudfactory/product/product-add'),
                meta: {
                    title: '编辑产品',
                    noCache: true
                },
                hidden: true
            },
            {
                path: 'productType',
                name: '产品类型管理',
                component: () =>
                    import ('@/views/cloudfactory/product/productType'),
                meta: {
                    title: '产品类型管理',
                    icon: 'el-icon-orange',
                    role: ['超级管理员']
                }
            },
            {
                path: 'productType-add',
                name: '添加产品类型',
                component: () =>
                    import ('@/views/cloudfactory/product/productType-add'),
                meta: {
                    title: '添加产品类型',

                    role: ['超级管理员']
                },
                hidden: true
            },

            {
                path: 'productType-edit/:id', //:id是占位符   
                name: '编辑产品类型',
                component: () =>
                    import ('@/views/cloudfactory/product/productType-add'),
                meta: {
                    title: '编辑产品类型',
                    noCache: true,
                    role: ['超级管理员']
                },
                hidden: true
            },
        ]
    },
    {
        path: '/device',
        component: Layout,
        // 设置/user的默认值
        redirect: '/device/device-info',
        name: '设备管理',
        meta: {
            title: '设备管理',
            icon: 'el-icon-video-camera',
            role: ['超级管理员']
        },
        children: [{
                path: 'device-info',
                name: '设备信息管理',
                component: () =>
                    import ('@/views/cloudfactory/device/device-info'),
                meta: {
                    title: '设备信息管理',
                    icon: 'el-icon-s-promotion',
                    role: ['超级管理员']
                }
            },
            {
                path: 'device-add',
                name: '添加设备',
                component: () =>
                    import ('@/views/cloudfactory/device/device-add'),
                meta: {
                    title: '添加设备',

                    role: ['超级管理员']
                },
                hidden: true
            },

            {
                path: 'device-edit/:id', //:id是占位符   
                name: '编辑设备',
                component: () =>
                    import ('@/views/cloudfactory/device/device-add'),
                meta: {
                    title: '编辑设备',
                    noCache: true,
                    role: ['超级管理员']
                },
                hidden: true
            },
            {
                path: 'deviceType',
                name: '设备类型管理',
                component: () =>
                    import ('@/views/cloudfactory/device/deviceType'),
                meta: {
                    title: '设备类型管理',
                    icon: 'el-icon-s-shop',
                    role: ['超级管理员']
                }
            },
            {
                path: 'deviceType-add',
                name: '添加设备类型',
                component: () =>
                    import ('@/views/cloudfactory/device/deviceType-add'),
                meta: {
                    title: '添加设备类型',

                    role: ['超级管理员']
                },
                hidden: true
            },

            {
                path: 'deviceType-edit/:id', //:id是占位符   
                name: '编辑设备类型',
                component: () =>
                    import ('@/views/cloudfactory/device/deviceType-add'),
                meta: {
                    title: '编辑设备类型',
                    noCache: true,
                    role: ['超级管理员']
                },
                hidden: true
            },
            {
                path: 'deviceHire',
                name: '设备租用信息',
                component: () =>
                    import ('@/views/cloudfactory/device/deviceHire-info'),
                meta: {
                    title: '设备租用信息',
                    icon: 'el-icon-office-building',
                    role: ['超级管理员']
                }
            },
        ]
    },

    {
        path: '/orderItemAdmin',
        component: Layout,
        redirect: '/orderItemAdmin/orderItemAdmin-info',
        name: '订单管理',
        meta: {
            title: '订单管理',

            role: ['超级管理员']
        },
        children: [{
                path: 'orderItemAdmin-info',
                name: '订单信息',
                component: () =>
                    import ('@/views/cloudfactory/order/orderItemAdmin-info'),
                meta: {
                    title: '订单信息',
                    icon: 'el-icon-date',
                    role: ['超级管理员']
                }
            },

        ]
    },
    {
        path: '/404',
        component: () =>
            import ('@/views/404'),
        hidden: true
    },
]

export const factoryRoutes = [{
        path: '/deviceFactory',
        component: Layout,
        redirect: '/deviceFactory/deviceFactory-info',
        name: '我的工厂',
        meta: {
            title: '我的工厂',
            icon: 'el-icon-discount',

            role: ['云工厂']
        },
        children: [{
                path: 'deviceFactory-info',
                name: '我的设备',
                component: () =>
                    import ('@/views/cloudfactory/device/deviceFactory-info'),
                meta: {
                    title: '我的设备',
                    icon: 'el-icon-s-promotion',

                    role: ['云工厂']
                }
            },
            {
                path: 'deviceUnhired-info',
                name: '租用中心',
                component: () =>
                    import ('@/views/cloudfactory/device/deviceUnhired-info'),
                meta: {
                    title: '租用中心',
                    icon: 'el-icon-office-building',
                    role: ['云工厂']
                }
            },
            {
                path: 'deviceFactory-add',
                name: '添加设备',
                component: () =>
                    import ('@/views/cloudfactory/device/deviceFactory-add'),
                meta: {
                    title: '添加设备',

                    role: ['云工厂']
                },
                hidden: true
            },

            {
                path: 'deviceFactory-edit/:id', //:id是占位符   
                name: '编辑设备',
                component: () =>
                    import ('@/views/cloudfactory/device/deviceFactory-add'),
                meta: {
                    title: '编辑设备',
                    noCache: true,
                    role: ['云工厂']
                },
                hidden: true
            },


            {
                path: 'deviceFactory-configure/:deviceName',
                name: '配置产能',
                component: () =>
                    import ('@/views/cloudfactory/device/deviceFactory-configure'),
                meta: {
                    title: '配置产能',
                    noCache: true,
                    role: ['云工厂']
                },
                hidden: true
            },
            {
                path: 'capacity-add/:deviceName', //:deviceName是占位符   
                name: '添加产能',
                component: () =>
                    import ('@/views/cloudfactory/device/capacity-add'),
                meta: {
                    title: '添加产能',
                    noCache: true,
                    role: ['云工厂']
                },
                hidden: true
            },
            {
                path: 'capacity-edit/:id', //:id是占位符   
                name: '修改产能',
                component: () =>
                    import ('@/views/cloudfactory/device/capacity-add'),
                meta: {
                    title: '修改产能',
                    noCache: true,
                    role: ['云工厂']
                },
                hidden: true
            },
        ]
    },

    {
        path: '/orderItemFactory',
        component: Layout,
        redirect: '/orderItemFactory/orderItemFactory-info',
        name: '订单管理',
        meta: {
            title: '订单管理',
            icon: 'el-icon-tickets',
            role: ['云工厂']
        },
        children: [{
                path: 'orderItemFactory-info',
                name: '订单接单',
                component: () =>
                    import ('@/views/cloudfactory/order/orderItemFactory-info'),
                meta: {
                    title: '订单接单',
                    icon: 'el-icon-document-add',
                    role: ['云工厂']
                }
            },


            {
                path: 'orderItemFactory-bidWin',
                name: '订单排产',
                component: () =>
                    import ('@/views/cloudfactory/order/orderItemFactory-bidWin'),
                meta: {
                    title: '订单排产',
                    icon: 'el-icon-document-checked',
                    role: ['云工厂']
                }
            },
            {
                path: 'orderItemFactory-schedule/:id',
                name: '排产信息',
                component: () =>
                    import ('@/views/cloudfactory/order/orderItemFactory-schedule'),
                meta: {
                    title: '排产信息',

                    role: ['云工厂']
                },
                hidden: true
            },

        ]
    }, {
        path: '/404',
        component: () =>
            import ('@/views/404'),
        hidden: true
    },
]

export const dealerRoutes = [


    {
        path: '/orderItemDealer',
        component: Layout,
        redirect: '/orderItemDealer/orderItemDealer-info',
        name: '我的订单',
        meta: {
            title: '我的订单',

            role: ['经销商']
        },
        children: [{
                path: 'orderItemDealer-info',
                name: '我的订单',
                component: () =>
                    import ('@/views/cloudfactory/order/orderItemDealer-info'),
                meta: {
                    title: '我的订单',
                    icon: 'el-icon-shopping-cart-full',
                    role: ['经销商']
                }
            },
            {
                path: 'orderItemDealer-add',
                name: '添加订单',
                component: () =>
                    import ('@/views/cloudfactory/order/orderItemDealer-add'),
                meta: {
                    title: '我的订单',

                    role: ['经销商']
                },
                hidden: true
            },

            {
                path: 'orderItemDealer-edit/:id', //:id是占位符   
                name: '编辑订单',
                component: () =>
                    import ('@/views/cloudfactory/order/orderItemDealer-add'),
                meta: {
                    title: '编辑订单',
                    noCache: true,
                    role: ['经销商']
                },
                hidden: true
            },
            {
                path: 'orderItemBid/:id', //:id是占位符   
                name: '竞标列表',
                component: () =>
                    import ('@/views/cloudfactory/order/orderItemBid'),
                meta: {
                    title: '竞标列表',
                    noCache: true,
                    role: ['经销商']
                },
                hidden: true
            },

        ]
    }, {
        path: '/404',
        component: () =>
            import ('@/views/404'),
        hidden: true
    },
]

export const endRoutes = [
    // 404 page must be placed at the end !!!
    {
        path: '*',
        redirect: '/',
        hidden: true
    }
]
const createRouter = () => new Router({
    // mode: 'history', // require service support
    scrollBehavior: () => ({
        y: 0
    }),
    routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
    const newRouter = createRouter()
    router.matcher = newRouter.matcher // reset router
    router.options.routes = constantRoutes;
}

export default router