package demo.ui

import web.events.Event
import web.events.EventTargetLike
import web.events.EventType
import web.events.addEventListener

/**
 * Subscribes [handler] to [type] on this target.
 *
 * One line over the typed `addEventListener`, kept because a dozen examples subscribe inline and
 * `element.addEventListener(EVENT) { … }` on each of them reads worse than the single-name form.
 */
fun <E : Event> EventTargetLike.listen(type: EventType<E>, handler: (E) -> Unit) {
    addEventListener(type, handler)
}
