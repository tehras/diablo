package com.github.tehras.ui.herodetails

import com.github.tehras.api.auth.oauth.OauthTokenProvider
import com.github.tehras.api.items.ItemsService
import com.github.tehras.base.arch.ObservableViewModel
import com.github.tehras.base.arch.executors.NetworkExecutor
import com.github.tehras.db.models.Item
import com.github.tehras.db.models.ItemDetails
import com.github.tehras.ui.herodetails.views.UiItemDetails
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.withLatestFrom
import javax.inject.Inject

class HeroItemDetailsViewModel @Inject constructor(
    private val itemService: ItemsService,
    private val tokenProvider: OauthTokenProvider,
    @NetworkExecutor private val ioExecutor: Scheduler
) :
    ObservableViewModel<HeroItemDetailsState, HeroItemDetailsUiEvent>() {

    override fun onCreate() {
        super.onCreate()

        val token = tokenProvider
            .oauthToken()
            .toObservable()

        val loadItemObservable = uiEvents()
            .ofType<HeroItemDetailsUiEvent.LoadItem>()
            .withLatestFrom(token)
            .map { it.first.item }
            .flatMapSingle {
                itemService
                    .getItem(it.tooltipParams.replace("/item/", "").replace(" ", "-"))
                    .subscribeOn(ioExecutor)
            }
            .subscribeOn(ioExecutor)

        loadItemObservable
            .map { HeroItemDetailsState(it.id, it, toHeroAttributes(it)) }
            .subscribeUntilDestroyed()
    }

    private fun toHeroAttributes(itemDetails: ItemDetails): List<UiItemDetails> {
        val uiHeroDetails = mutableListOf<UiItemDetails>()

        val primary = itemDetails.attributes.primary
        if (primary.isNotEmpty()) {
            uiHeroDetails.add(UiItemDetails.Title("Primary"))
            uiHeroDetails.addAll(primary.map { UiItemDetails.Details(it) })
        }

        val secondary = itemDetails.attributes.secondary
        if (secondary.isNotEmpty()) {
            uiHeroDetails.add(UiItemDetails.Title("Secondray"))
            uiHeroDetails.addAll(secondary.map { UiItemDetails.Details(it) })
        }

        val randomAffixes = itemDetails
            .randomAffixes
            .map {
                it.oneOf.map { oneOf ->
                    UiItemDetails.Details(oneOf)
                }
            }
            .flatten()

        uiHeroDetails.addAll(randomAffixes)

        return uiHeroDetails
    }
}

data class HeroItemDetailsState(
    val itemId: String,
    val heroItemDetails: ItemDetails,
    val heroAttributes: List<UiItemDetails>
)

sealed class HeroItemDetailsUiEvent {
    data class LoadItem(val item: Item) : HeroItemDetailsUiEvent()
}
