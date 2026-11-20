package demo.examples.overview

import demo.CatalogEntry
import io.github.shijianjs.ktjs.dom.css.dsl.invoke
import io.github.shijianjs.ktjs.dom.element.invoke
import io.github.shijianjs.ktjs.vaadin.card.CardElement
import web.html.HTMLAnchorElement
import web.html.HTMLElement

/** The card and the link around it; a language switch rewrites the title and the summary. */
internal class Card(
    val entry: CatalogEntry,
    val node: CardElement,
    val link: HTMLAnchorElement,
    val summary: HTMLElement,
)
