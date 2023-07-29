package com.example.api

// TODO: Should this whole API be versioned in prod? How will I manage that?
//  Should every class be open?
object AStar {

    // TODO: I decided to set costs on all relevant Nodes immediately before AStar.route(),
    //  rather than go the way of passing functions. I may change my mind on this.
    //  Yeah. At some point there'll be Paths with stale Nodes and you'll need to remember to
    //  discard them immediately. It's a mess.
    open class Node(var vector: CellVector, val neighbours: Set<Node>, var cost: Int?)

    class Path(val route: Array<Node>, val cost: Int)

    // FIXME: Inefficient data structure for this purpose.
    private val frontier = mutableMapOf<Node, Int>()

    fun findPath(start: Node, destination: Node): Path? {
        // Return null early if destination is unreachable.
        if (destination.cost == null) return null

        queueIn(start, 0)

        val cameFrom = mutableMapOf<Node, Node>()
        val bestRouteCosts = mutableMapOf<Node, Int>()
        bestRouteCosts[start] = 0

        while (frontier.isNotEmpty()) {
            // Get the highest-priorty cell from the frontier.
            val current = queueOut()

            // If we've reached the destination, return the path and its cost. If not, keep working.
            if (current == destination) {
                val route = mutableListOf<Node>()
                var traceNode: Node? = destination

                while (traceNode != start) {
                    traceNode?.run {
                        route.add(this)
                    }
                    traceNode = cameFrom[traceNode]
                }
                route.reverse()

                return Path(route.toTypedArray(), bestRouteCosts[current]!!)
            }

            // For each neighbour to this cell,
            current.neighbours.forEach { neighbour ->
                // Proceed if traversable...
                neighbour.cost?.run {
                    val newCost = bestRouteCosts[current]!! + this
                    // If neighbour hasn't been checked, or this path to it has a lower cost,
                    if (!bestRouteCosts.contains(neighbour) || newCost < bestRouteCosts[neighbour]!!) {
                        // store the new cost of moving to neighbour,
                        bestRouteCosts[neighbour] = newCost
                        // put neighbour in the frontier with its checking priority (lower is better),
                        val priority = newCost + neighbour.vector.manDist(destination.vector)
                        queueIn(neighbour, priority)
                        // and store the path.
                        cameFrom[neighbour] = current
                    }
                }
            }

        }

        // No path
        return null
    }

    private fun queueIn(node: Node, priority: Int) {
        frontier[node] = priority
    }

    private fun queueOut(): Node {
        // Assumes the frontier will never be empty when this is called (or else throws)
        // Returns the CellVector with the lowest priority value (highest actual priority)
        val sorted = frontier.entries.sortedBy { it.value }
        val node = sorted.first().key
        frontier.remove(node)
        return node
    }
}