/*
 * Copyright 2021 http4s.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.http4s.dom

import cats.effect.Async
import org.http4s.client.defaults
import scala.concurrent.duration._

sealed abstract class WSClientBuilder[F[_]] private (
    val connectionTimeout: Duration,
    val operationTimeout: Duration
)(implicit protected val F: Async[F]) {

  private def copy(
      connectionTimeout: Duration = connectionTimeout,
      operationTimeout: Duration = operationTimeout
  ): WSClientBuilder[F] =
    new WSClientBuilder[F](connectionTimeout, operationTimeout) {}

  def withConnectionTimeout(timeout: Duration): WSClientBuilder[F] =
    copy(connectionTimeout = timeout)

  def withOperationTimeout(timeout: Duration): WSClientBuilder[F] =
    copy(operationTimeout = timeout)

  def create: WSClient[F] = WSClient.makeClient(connectionTimeout, operationTimeout)
}

object WSClientBuilder {
  def apply[F[_]: Async]: WSClientBuilder[F] =
    new WSClientBuilder[F](
      connectionTimeout = defaults.RequestTimeout,
      operationTimeout = defaults.RequestTimeout
    ) {}
}
