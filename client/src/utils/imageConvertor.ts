export function convertImageFromDataURL(dataURL: string): Blob {
  const matches = dataURL.match(/^data:(.*?);base64,(.*)$/)

  if (!matches) {
    throw new Error('Invalid data URL.')
  }

  // const fileExtension = matches[1].split('/')[1]
  const base64Data = matches[2]

  const byteCharacters = atob(base64Data)
  const byteArrays = []

  for (let offset = 0; offset < byteCharacters.length; offset += 512) {
    const slice = byteCharacters.slice(offset, offset + 512)

    const byteNumbers = new Array(slice.length)
    for (let i = 0; i < slice.length; i++) {
      byteNumbers[i] = slice.charCodeAt(i)
    }

    const byteArray = new Uint8Array(byteNumbers)
    byteArrays.push(byteArray)
  }

  return new Blob(byteArrays, { type: matches[1] })
}
