import axios from 'axios'

export const getHistory = async (walkLogId: string) => {
  try {
    const response = await axios.get(`/api/walk-logs/${walkLogId}`, {
      headers: {
        'Content-Type': 'application/json',
        'ngrok-skip-browser-warning': '69420',
      },
    })
    return response.data
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}
