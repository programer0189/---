// 用户认证相关工具函数


export const isLoggedIn = () => {
  const loginUser = localStorage.getItem('loginUser')
  if (!loginUser) return false
  
  try {
    const user = JSON.parse(loginUser)
    return !!(user && user.id && user.token && user.username)
  } catch (error) {
    console.error('解析用户信息失败:', error)
    localStorage.removeItem('loginUser')
    return false
  }
}



export const getCurrentUser = () => {
  const loginUser = localStorage.getItem('loginUser')
  if (!loginUser) return null
  
  try {
    const user = JSON.parse(loginUser)
    if (user && user.id && user.token && user.username) {
      return user
    }
    return null
  } catch (error) {
    console.error('解析用户信息失败:', error)
    localStorage.removeItem('loginUser')
    return null
  }
}

/**

 */
export const clearUserAuth = () => {
  localStorage.removeItem('loginUser')
}

/**
 * 设置用户登录信息

 */
export const setUserAuth = (userInfo) => {
  if (userInfo && userInfo.id && userInfo.token && userInfo.username) {
    localStorage.setItem('loginUser', JSON.stringify(userInfo))
  }
}

/**
 * 检查token是否有效（可以根据实际需求扩展）

 */
export const isTokenValid = () => {
  const user = getCurrentUser()
  if (!user) return false
  

  
  return true
}
