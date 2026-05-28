# My Wardrobe Product Design

Date: 2026-05-28
Status: Draft for user review

## Purpose

My Wardrobe is a local-first Android app for cataloging clothes from photos and quickly checking what exists in a personal wardrobe. The first target device is a Samsung Galaxy Z Flip 5. The app is for private/local use and is not intended for store publication in the first version.

The product should help the user answer two questions with minimal effort:

- What clothes do I already have?
- Can I quickly find a specific item by photo, category, color, or occasion?

## Product Principles

- Photo-first: the clothing photo is the primary record, not a long form.
- Low-friction capture: adding clothes must feel fast enough for a real wardrobe cleanup session.
- Searchable by default: the home screen should make the wardrobe feel immediately browsable and searchable.
- Local and private: V1 assumes data stays on the device.
- Future-ready, not future-heavy: leave room for outfit suggestions and richer metadata later, but do not burden V1.

## V1 Scope

V1 focuses on two jobs:

1. Catalog clothes from photos.
2. Search and browse the catalog quickly.

The app supports both single-item entry and batch capture:

- Single-item entry is for adding one garment carefully.
- Batch capture is for quickly photographing many garments and tagging them later.

## Primary User Experience

The app opens to the Wardrobe screen: a searchable visual grid of saved clothing items. The user can search, browse by category, apply filters, open item details, or tap the camera action to add more clothes.

The main navigation has four core surfaces:

- Wardrobe
- Capture
- Review Queue
- Item Detail

## First-Use Flow

1. Show a short local/private introduction.
2. Ask the user to add the first item or start batch capture.
3. Let the user choose or accept default clothing categories.
4. Land on the Wardrobe screen after the first saved item or skipped setup.

The introduction should be practical and brief. It should not feel like a marketing page.

## Screen Design

### Wardrobe

The Wardrobe screen is the default home screen.

Core elements:

- Search bar at the top.
- Category chips for quick browsing.
- Filter action for structured filtering.
- Photo grid of wardrobe items.
- Camera action for adding clothes.

Search should match:

- Name
- Category
- Color
- Occasion
- Fabric
- Season
- Notes

The grid should prioritize photos and short labels. It should support empty, loading, and no-results states.

### Capture

The Capture screen supports two modes:

- Add One: take or select one photo, then fill the item form.
- Batch Capture: take or select multiple photos, then send them to the Review Queue.

V1 should support both camera capture and gallery import. Camera capture is useful during wardrobe cleanup, while gallery import keeps the app practical when photos already exist.

Batch capture should avoid forcing metadata entry during the photo-taking session. Its job is speed.

### Review Queue

The Review Queue contains captured photos that are not fully cataloged yet.

Each queued item should show:

- Photo preview
- Missing required fields
- Quick actions to save, skip, or delete

The queue should ask for required fields first, then expose optional fields below. This keeps cleanup focused.

### Item Detail

The Item Detail screen shows and edits one wardrobe item.

It should include:

- Main photo
- Required fields
- Optional fields
- Notes
- Delete action

The screen should answer "what is this?" and "when would I use it?" without becoming a shopping or finance database.

## Data Fields

Required fields:

- Photo
- Name
- Category
- Color

Optional V1 fields:

- Occasion
- Fabric
- Season
- Notes

Future fields:

- Brand
- Purchase date
- Price
- Size
- Care instructions
- Worn count
- Last worn date
- Outfit combinations

## Suggested Initial Categories

The first version should support editable categories, with sensible defaults:

- Saree
- Kurti
- Top
- Dress
- Dupatta
- Bottom
- Jacket or outerwear
- Accessory
- Other

The app should not assume the wardrobe belongs only to women, but the defaults should work well for the first intended user.

## Filters

V1 filters should include:

- Category
- Color
- Occasion
- Fabric
- Season

Filters should be optional. The basic search and visual grid should remain useful without meticulous tagging.

## Non-Goals For V1

The following are intentionally out of scope for V1:

- Cloud sync
- User accounts
- Social sharing
- Store publication
- AI outfit generation
- Automatic clothing detection from wardrobe shelf photos
- Purchase tracking
- Worn-history analytics

## Roadmap

Future improvements can include:

- Outfit suggestions based on occasion, color, and season.
- Worn history and repeat tracking.
- Rich purchase and care metadata.
- Image-based duplicate detection.
- Backup/export.
- Automatic category or color suggestions from photos.

## Success Criteria

V1 is successful if:

- A user can add a single clothing item in under one minute.
- A user can batch capture many items without filling forms between photos.
- A user can search for an item by common terms such as "blue saree" or "black kurti".
- The wardrobe grid feels useful with only required fields filled.
- Optional fields improve search without making capture feel heavy.

## Open Decisions For Implementation Planning

- Native Android technology choice.
- Local database choice.
- Image storage strategy.
- How much visual polish to apply before the first usable build.
