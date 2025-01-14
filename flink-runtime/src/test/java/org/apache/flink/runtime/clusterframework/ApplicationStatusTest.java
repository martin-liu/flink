/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.runtime.clusterframework;

import org.apache.flink.util.TestLoggerExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

/** Tests for the {@link ApplicationStatus}. */
@ExtendWith(TestLoggerExtension.class)
public class ApplicationStatusTest {

    private static final int SUCCESS_EXIT_CODE = 0;

    @Test
    public void succeededStatusMapsToSuccessExitCode() {
        int exitCode = ApplicationStatus.SUCCEEDED.processExitCode();
        assertThat(exitCode).isEqualTo(SUCCESS_EXIT_CODE);
    }

    @Test
    public void cancelledStatusMapsToSuccessExitCode() {
        int exitCode = ApplicationStatus.CANCELED.processExitCode();
        assertThat(exitCode).isEqualTo(SUCCESS_EXIT_CODE);
    }

    @Test
    public void notSucceededNorCancelledStatusMapsToNonSuccessExitCode() {
        Iterable<Integer> exitCodes = exitCodes(notSucceededNorCancelledStatus());
        assertThat(exitCodes).doesNotContain(SUCCESS_EXIT_CODE);
    }

    private static Iterable<Integer> exitCodes(Iterable<ApplicationStatus> statuses) {
        return StreamSupport.stream(statuses.spliterator(), false)
                .map(ApplicationStatus::processExitCode)
                .collect(Collectors.toList());
    }

    private static Iterable<ApplicationStatus> notSucceededNorCancelledStatus() {
        return Arrays.stream(ApplicationStatus.values())
                .filter(ApplicationStatusTest::isNotSucceededNorCancelled)
                .collect(Collectors.toList());
    }

    private static boolean isNotSucceededNorCancelled(ApplicationStatus status) {
        return status != ApplicationStatus.SUCCEEDED && status != ApplicationStatus.CANCELED;
    }
}
