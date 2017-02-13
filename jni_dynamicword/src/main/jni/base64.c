/** Copyright (C) 2003 MySQL AB

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; version 2 of the License.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA */

#include "base64.h"
static char base64_table[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                             "abcdefghijklmnopqrstuvwxyz"
                             "0123456789+/";

int
base64_needed_encoded_length(int length_of_data)
{
  int nb_base64_chars;
  nb_base64_chars= (length_of_data + 2) / 3 * 4;

  return
    nb_base64_chars +            /** base64 char incl padding */
    (nb_base64_chars - 1)/ 76 +  /** newlines */
    1;                           /** NUL termination of string */
}


int
base64_needed_decoded_length(int length_of_encoded_data)
{
  return (int) ceil(length_of_encoded_data * 3 / 4);
}


/**
  Encode a data as base64.

  Note: We require that dst is pre-allocated to correct size.
        See base64_needed_encoded_length().
*/

int
base64_encode(const void *src, unsigned int src_len, char *dst)
{
  const unsigned char *s= (const unsigned char*)src;
  unsigned int i= 0;
  unsigned int len= 0;

  for (; i < src_len; len += 4)
  {
    unsigned c;

    if (len == 76)
    {
      len= 0;
      *dst++= '\n';
    }

    c= s[i++];
    c <<= 8;

    if (i < src_len)
      c += s[i];
    c <<= 8;
    i++;

    if (i < src_len)
      c += s[i];
    i++;

    *dst++= base64_table[(c >> 18) & 0x3f];
    *dst++= base64_table[(c >> 12) & 0x3f];

    if (i > (src_len + 1))
      *dst++= '=';
    else
      *dst++= base64_table[(c >> 6) & 0x3f];

    if (i > src_len)
      *dst++= '=';
    else
      *dst++= base64_table[(c >> 0) & 0x3f];
  }
  *dst= '\0';

  return 0;
}


static inline unsigned int
pos(unsigned char c)
{
  return (unsigned int) (strchr(base64_table, c) - base64_table);
}


/**
  Decode a base64 string

  SYNOPSIS
    base64_decode()
    src      Pointer to base64-encoded string
    len      Length of string at 'src'
    dst      Pointer to location where decoded data will be stored
    end_ptr  Pointer to variable that will refer to the character
             after the end of the encoded data that were decoded. Can
             be NULL.

  DESCRIPTION

    The base64-encoded data in the range ['src','*end_ptr') will be
    decoded and stored starting at 'dst'.  The decoding will stop
    after 'len' characters have been read from 'src', or when padding
    occurs in the base64-encoded data. In either case: if 'end_ptr' is
    non-null, '*end_ptr' will be set to point to the character after
    the last read character, even in the presence of error.

  NOTE
    We require that 'dst' is pre-allocated to correct size.

  SEE ALSO
    base64_needed_decoded_length().

  RETURN VALUE
    Number of bytes written at 'dst' or -1 in case of failure
*/
int
base64_decode(const char *src_base, unsigned int len,
              void *dst, const char **end_ptr)
{
  char b[3];
  unsigned int i= 0;
  char *dst_base= (char *)dst;
  char const *src= src_base;
  char *d= dst_base;
  unsigned int j;

  while (i < len)
  {
    unsigned c= 0;
    unsigned int mark= 0;


    c += pos(*src++);
    c <<= 6;
    i++;

    c += pos(*src++);
    c <<= 6;
    i++;


    if (*src != '=')
      c += pos(*src++);
    else
    {
      src += 2;                /** There should be two bytes padding */
      i= len;
      mark= 2;
      c <<= 6;
      goto end;
    }
    c <<= 6;
    i++;


    if (*src != '=')
      c += pos(*src++);
    else
    {
      src += 1;                 /** There should be one byte padding */
      i= len;
      mark= 1;
      goto end;
    }
    i++;

  end:
    b[0]= (c >> 16) & 0xff;
    b[1]= (c >>  8) & 0xff;
    b[2]= (c >>  0) & 0xff;

    for (j=0; j<3-mark; j++)
      *d++= b[j];
  }

  if (end_ptr != 0)
    *end_ptr= src;

  /**
    The variable 'i' is set to 'len' when padding has been read, so it
    does not actually reflect the number of bytes read from 'src'.
   */
  return i != len ? -1 : (int) (d - dst_base);
}


