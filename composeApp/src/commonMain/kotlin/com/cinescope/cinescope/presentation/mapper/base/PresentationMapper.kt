package com.cinescope.cinescope.presentation.mapper.base

/**
 * Base interface for presentation mappers.
 *
 * Defines unidirectional mapping from domain models to presentation models.
 * Presentation models should not be mapped back to domain models, as they
 * are optimized for display only and may lose information during transformation.
 *
 * Presentation mappers are responsible for:
 * - Converting domain data to UI-friendly formats
 * - Pre-computing expensive operations (e.g., full image URLs)
 * - Formatting dates, numbers, and other display values
 * - Removing platform-specific types (e.g., kotlinx.datetime.Instant → String)
 *
 * @param Domain The domain model type
 * @param Presentation The presentation model type
 */
interface PresentationMapper<in Domain, out Presentation> {
    /**
     * Maps a single domain model to a presentation model.
     *
     * @param domain The domain model to map
     * @return The corresponding presentation model
     */
    fun toPresentation(domain: Domain): Presentation

    /**
     * Maps a list of domain models to presentation models.
     *
     * Default implementation delegates to toPresentation for each item.
     * Override this if you need custom list-level logic.
     *
     * @param domainList The list of domain models to map
     * @return List of corresponding presentation models
     */
    fun toPresentation(domainList: List<Domain>): List<Presentation> {
        return domainList.map { toPresentation(it) }
    }
}
