package demo.examples.overview

import io.github.shijianjs.ktjs.demo.page.Blocks.heading
import io.github.shijianjs.ktjs.dom.css.dsl.invoke
import io.github.shijianjs.ktjs.dom.element.invoke
import web.html.HTMLElement

/** One group of the index: the heading (mark and text are separate nodes) and its cards. */
internal class GroupSection(val title: HTMLElement, val label: HTMLElement, val cards: List<Card>)
