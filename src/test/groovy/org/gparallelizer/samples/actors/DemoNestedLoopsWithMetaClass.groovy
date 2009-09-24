//  GParallelizer
//
//  Copyright © 2008-9  The original author or authors
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License. 

package org.gparallelizer.samples.actors

import org.gparallelizer.actor.Actor
import org.gparallelizer.actor.Actors
import static org.gparallelizer.actor.Actors.actor

/**
 * Demonstrates a way to do continuation-style loops with Actors.
 * @author Vaclav Pech
 */

Actor actor = actor {
    outerLoop()
}

actor.metaClass {
    outerLoop = {->
        react {a ->
            println 'Outer: ' + a
            innerLoop()
        }
    }

    innerLoop = {->
        react {b ->
            println 'Inner ' + b
            if (b==0) outerLoop()
            else innerLoop()
        }
    }
}

actor.start()

actor.send 1
actor.send 1
actor.send 1
actor.send 1
actor.send 1
actor.send 0
actor.send 2
actor.send 2
actor.send 2
actor.send 2
actor.send 2
actor.send 0
actor.send 3
actor.send 3
actor.send 3
actor.send 3

Thread.sleep 5000
Actors.defaultPooledActorGroup.shutdown()

