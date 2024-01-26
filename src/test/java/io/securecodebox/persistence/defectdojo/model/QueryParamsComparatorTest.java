// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link QueryParamsComparator}
 */
class QueryParamsComparatorTest {
  @Test
  void isNull() {
    assertAll(
      () -> assertThat(QueryParamsComparator.isNull(null), is(true)),
      () -> assertThat(QueryParamsComparator.isNull(Collections.emptyMap()), is(false))
    );
  }

  @Test
  void isIdEqual_falseIfModelIsNull() {
    assertThat(
      QueryParamsComparator.isIdEqual(null, Collections.emptyMap()),
      is(false));
  }

  @Test
  void isIdEqual_falseIfQueryParamsIsNull() {
    assertThat(
      QueryParamsComparator.isIdEqual(mock(HasId.class), null),
      is(false));
  }

  @Test
  void isIdEqual_falseIfQueryParamsDoesNotContainId() {
    assertThat(
      QueryParamsComparator.isIdEqual(mock(HasId.class), Collections.emptyMap()),
      is(false));
  }

  @Test
  void isIdEqual_falseIfQueryParamValueIsNull() {
    final var queryParams = new HashMap<String, Object>();
    queryParams.put(QueryParamsComparator.QUERY_PARAM_KEY_FOR_ID, null);

    assertThat(
      QueryParamsComparator.isIdEqual(mock(HasId.class), queryParams),
      is(false));
  }

  @Test
  void isIdEqual_trueForSameNumbers() {
    final var model = mock(HasId.class);
    when(model.getId()).thenReturn(42L);

    final var queryParams = new HashMap<String, Object>();
    queryParams.put(QueryParamsComparator.QUERY_PARAM_KEY_FOR_ID, 42L);

    assertThat(
      QueryParamsComparator.isIdEqual(model, queryParams),
      is(true));
  }

  @Test
  void isIdEqual_falseForDifferentNumbers() {
    final var model = mock(HasId.class);
    when(model.getId()).thenReturn(42L);

    final var queryParams = new HashMap<String, Object>();
    queryParams.put(QueryParamsComparator.QUERY_PARAM_KEY_FOR_ID, 23L);

    assertThat(
      QueryParamsComparator.isIdEqual(mock(HasId.class), queryParams),
      is(false));
  }

  @Test
  void isNameEqual_falseIfModelIsNull() {
    assertThat(
      QueryParamsComparator.isNameEqual(null, Collections.emptyMap()),
      is(false));
  }

  @Test
  void isNameEqual_falseIfQueryParamsIsNull() {
    assertThat(
      QueryParamsComparator.isNameEqual(mock(HasName.class), null),
      is(false));
  }

  @Test
  void isNameEqual_falseIfQueryParamsDoesNotContainId() {
    assertThat(
      QueryParamsComparator.isNameEqual(mock(HasName.class), Collections.emptyMap()),
      is(false));
  }

  @Test
  void isNameEqual_falseIfQueryParamValueIsNull() {
    final var queryParams = new HashMap<String, Object>();
    queryParams.put(QueryParamsComparator.QUERY_PARAM_KEY_FOR_NAME, null);

    assertThat(
      QueryParamsComparator.isNameEqual(mock(HasName.class), queryParams),
      is(false));
  }

  @Test
  void isNameEqual_trueForSameValues() {
    final var model = mock(HasName.class);
    when(model.getName()).thenReturn("foobar");

    final var queryParams = new HashMap<String, Object>();
    queryParams.put(QueryParamsComparator.QUERY_PARAM_KEY_FOR_NAME, "foobar");

    assertThat(
      QueryParamsComparator.isNameEqual(model, queryParams),
      is(true));
  }

  @Test
  void isNameEqual_falseForDifferentValues() {
    final var model = mock(HasName.class);
    when(model.getName()).thenReturn("foobar");

    final var queryParams = new HashMap<String, Object>();
    queryParams.put(QueryParamsComparator.QUERY_PARAM_KEY_FOR_NAME, "snafu");

    assertThat(
      QueryParamsComparator.isNameEqual(model, queryParams),
      is(false));
  }
}
