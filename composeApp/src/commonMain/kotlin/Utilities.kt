fun showHideSightingPicker(input: AddSighting): AddSighting {
    if (input != AddSighting.NONE) return AddSighting.NONE
    return input.next()
}
