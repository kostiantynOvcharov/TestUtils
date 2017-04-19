package com.scc.ptl.testutils;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Formatter;

public final class StringBoundValueProvider extends BoundValueProvider<String>
{
   private final static String WHITESPACE = " ";
   private final static String TAB = "\u0009";

   public StringBoundValueProvider(String placeHolder, boolean treatBlankStringAsEmpty, Range<Integer> lengthBounds)
   {
      super(String.class,
            getMinValues(placeHolder, treatBlankStringAsEmpty, lengthBounds.lowerEndpoint()),
            getOverLowerBoundValues(placeHolder, lengthBounds.lowerEndpoint()),
            getUnderLowerBoundValues(placeHolder, treatBlankStringAsEmpty, lengthBounds.lowerEndpoint()),
            getMaxValues(placeHolder, lengthBounds.upperEndpoint()),
            getOverUpperBoundValues(placeHolder, lengthBounds.upperEndpoint()),
            getUnderUpperBoundValues(placeHolder, treatBlankStringAsEmpty, lengthBounds.upperEndpoint()));

      //TODO update to allow usage open bounds
      if (lengthBounds.lowerBoundType() == BoundType.OPEN || lengthBounds.upperBoundType() == BoundType.OPEN)
      {
         throw new IllegalArgumentException("String range should not be open");
      }
   }

   public StringBoundValueProvider(String format, String placeHolder, boolean treatBlankStringAsEmpty, Range<Integer> lengthBounds)
   {
      super(String.class,
            getMinValues(format, placeHolder, treatBlankStringAsEmpty, lengthBounds.lowerEndpoint()),
            getOverLowerBoundValues(format, placeHolder, lengthBounds.lowerEndpoint()),
            getUnderLowerBoundValues(format, placeHolder, treatBlankStringAsEmpty, lengthBounds.lowerEndpoint()),
            getMaxValues(format, placeHolder, lengthBounds.upperEndpoint()),
            getOverUpperBoundValues(format, placeHolder, lengthBounds.upperEndpoint()),
            getUnderUpperBoundValues(format, placeHolder, treatBlankStringAsEmpty, lengthBounds.upperEndpoint()));

      //TODO update to allow usage open ranges
      if (lengthBounds.lowerBoundType() == BoundType.OPEN || lengthBounds.upperBoundType() == BoundType.OPEN)
      {
         throw new IllegalArgumentException("String range should not be open");
      }
   }

   private static Iterable<String> getMinValues(String placeHolder, boolean treatBlankStringAsEmpty, Integer minValue)
   {
      Collection<String> minValues = new ArrayList<String>();
      if (minValue == null)
      {
         return minValues;
      }
      if (minValue == 0)
      {
         minValues.add(StringUtils.EMPTY);
         if (treatBlankStringAsEmpty)
         {
            minValues.add(WHITESPACE);
            minValues.add(TAB);
         }
      }
      if (minValue > 0)
      {
         minValues.add(StringUtils.repeat(placeHolder, minValue));
      }
      return minValues;
   }

   private static Iterable<String> getMaxValues(String placeHolder, Integer maxValue)
   {
      return maxValue != null ? new ArrayList<String>(Arrays.asList(StringUtils.repeat(placeHolder, maxValue))) : new ArrayList<String>();
   }

   private static Iterable<String> getOverLowerBoundValues(String placeHolder, Integer minValue)
   {
      return minValue != null ?
            new ArrayList<String>(Arrays.asList(StringUtils.repeat(placeHolder, minValue + 1))) :
            new ArrayList<String>();
   }

   private static Iterable<String> getUnderLowerBoundValues(String placeHolder, boolean treatBlankStringAsEmpty, Integer minValue)
   {
      Collection<String> underLowerValues = new ArrayList<String>();
      if (minValue == null)
      {
         return underLowerValues;
      }
      if (minValue == 1)
      {
         underLowerValues.add(StringUtils.EMPTY);
         if (treatBlankStringAsEmpty)
         {
            underLowerValues.add(WHITESPACE);
            underLowerValues.add(TAB);
         }
      }
      if (minValue > 1)
      {
         underLowerValues.add(StringUtils.repeat(placeHolder, minValue - 1));
      }
      return underLowerValues;
   }

   private static Iterable<String> getOverUpperBoundValues(String placeHolder, Integer maxValue)
   {
      return maxValue != null ?
            new ArrayList<String>(Arrays.asList(StringUtils.repeat(placeHolder, maxValue + 1))) :
            new ArrayList<String>();
   }

   private static Iterable<String> getUnderUpperBoundValues(String placeHolder, boolean treatBlankStringAsEmpty, Integer maxValue)
   {
      Collection<String> underUpperValues = new ArrayList<String>();
      if (maxValue == null)
      {
         return underUpperValues;
      }
      if (maxValue == 1)
      {
         underUpperValues.add(StringUtils.EMPTY);
         if (treatBlankStringAsEmpty)
         {
            underUpperValues.add(WHITESPACE);
            underUpperValues.add(TAB);
         }
      }
      if (maxValue > 1)
      {
         underUpperValues.add(StringUtils.repeat(placeHolder, maxValue - 1));
      }
      return underUpperValues;
   }

   private static Iterable<String> getMinValues(String format, String placeHolder, boolean treatBlankStringAsEmpty, Integer minValue)
   {
      Collection<String> minValues = new ArrayList<String>();
      if (minValue == null || StringUtils.isBlank(format))
      {
         return minValues;
      }
      int constantPartLength = getConstantPartLength(format, placeHolder);
      if (constantPartLength > minValue)
      {
         return minValues;
      }
      if (minValue == 0)
      {
         minValues.add(StringUtils.EMPTY);
         if (treatBlankStringAsEmpty)
         {
            minValues.add(WHITESPACE);
            minValues.add(TAB);
         }
      }
      if (minValue > 0)
      {
         String repeatedPlaceHolder = StringUtils.repeat(placeHolder, minValue - constantPartLength);
         minValues.add(new Formatter().format(format, repeatedPlaceHolder).toString());
      }
      return minValues;
   }

   private static Iterable<String> getMaxValues(String format, String placeHolder, Integer maxValue)
   {
      Collection<String> maxValues = new ArrayList<String>();
      if (maxValue == null || StringUtils.isBlank(format))
      {
         return maxValues;
      }
      int constantPartLength = getConstantPartLength(format, placeHolder);
      if (constantPartLength > maxValue)
      {
         return maxValues;
      }
      if (maxValue > 0)
      {
         String repeatedPlaceHolder = StringUtils.repeat(placeHolder, maxValue - constantPartLength);
         maxValues.add(new Formatter().format(format, repeatedPlaceHolder).toString());
      }
      return maxValues;
   }

   private static Iterable<String> getOverLowerBoundValues(String format, String placeHolder, Integer minValue)
   {
      Collection<String> overLowerValues = new ArrayList<String>();
      if (minValue == null || StringUtils.isBlank(format))
      {
         return overLowerValues;
      }
      int constantPartLength = getConstantPartLength(format, placeHolder);
      if (constantPartLength > minValue + 1)
      {
         overLowerValues.add(new Formatter().format(format, placeHolder).toString());
         return overLowerValues;
      }
      if (minValue > 0)
      {
         String repeatedPlaceHolder = StringUtils.repeat(placeHolder, minValue + 1 - constantPartLength);
         overLowerValues.add(new Formatter().format(format, repeatedPlaceHolder).toString());
      }
      return overLowerValues;
   }

   private static Iterable<String> getUnderLowerBoundValues(String format, String placeHolder, boolean treatBlankStringAsEmpty,
         Integer minValue)
   {
      Collection<String> underLowerValues = new ArrayList<String>();
      if (minValue == null || StringUtils.isBlank(format))
      {
         return underLowerValues;
      }
      int constantPartLength = getConstantPartLength(format, placeHolder);
      if (constantPartLength > minValue - 1)
      {
         return underLowerValues;
      }
      if (minValue == 1)
      {
         underLowerValues.add(StringUtils.EMPTY);
         if (treatBlankStringAsEmpty)
         {
            underLowerValues.add(WHITESPACE);
            underLowerValues.add(TAB);
         }
      }
      if (minValue > 1)
      {
         String repeatedPlaceHolder = StringUtils.repeat(placeHolder, minValue - 1 - constantPartLength);
         underLowerValues.add(new Formatter().format(format, repeatedPlaceHolder).toString());
      }
      return underLowerValues;
   }

   private static Iterable<String> getOverUpperBoundValues(String format, String placeHolder, Integer maxValue)
   {
      Collection<String> overUpperValues = new ArrayList<String>();
      if (maxValue == null || StringUtils.isBlank(format))
      {
         return overUpperValues;
      }
      int constantPartLength = getConstantPartLength(format, placeHolder);
      if (constantPartLength > maxValue + 1)
      {
         overUpperValues.add(new Formatter().format(format, placeHolder).toString());
         return overUpperValues;
      }
      if (maxValue > 0)
      {
         String repeatedPlaceHolder = StringUtils.repeat(placeHolder, maxValue + 1 - constantPartLength);
         overUpperValues.add(new Formatter().format(format, repeatedPlaceHolder).toString());
      }
      return overUpperValues;
   }

   private static Iterable<String> getUnderUpperBoundValues(String format, String placeHolder, boolean treatBlankStringAsEmpty,
         Integer maxValue)
   {
      Collection<String> underUpperValues = new ArrayList<String>();
      if (maxValue == null || StringUtils.isBlank(format))
      {
         return underUpperValues;
      }
      int constantPartLength = getConstantPartLength(format, placeHolder);
      if (constantPartLength > maxValue - 1)
      {
         return underUpperValues;
      }
      if (maxValue == 1)
      {
         underUpperValues.add(StringUtils.EMPTY);
         if (treatBlankStringAsEmpty)
         {
            underUpperValues.add(WHITESPACE);
            underUpperValues.add(TAB);
         }
      }
      if (maxValue > 1)
      {
         String repeatedPlaceHolder = StringUtils.repeat(placeHolder, maxValue - 1 - constantPartLength);
         underUpperValues.add(new Formatter().format(format, repeatedPlaceHolder).toString());
      }
      return underUpperValues;
   }

   private static int getConstantPartLength(String format, String placeHolder)
   {
      Formatter formatter = new Formatter();
      formatter.format(format, placeHolder);
      int constantPartLength = 0;
      int lengthDiff = format.length() - formatter.toString().length();
      if (lengthDiff == 0 && placeHolder.length() == 2)
      {
         constantPartLength = format.length() - 2;
      }
      else if (lengthDiff == 0)
      {
         constantPartLength = format.length();
      }
      else if (lengthDiff >= 1)
      {
         constantPartLength = format.length() - 2;
      }
      return constantPartLength;
   }
}
