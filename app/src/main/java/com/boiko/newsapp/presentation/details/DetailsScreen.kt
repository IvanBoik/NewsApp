package com.boiko.newsapp.presentation.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.boiko.newsapp.R
import com.boiko.newsapp.domain.model.Article
import com.boiko.newsapp.domain.model.Source
import com.boiko.newsapp.presentation.Dimens.ArticleImageHeight
import com.boiko.newsapp.presentation.Dimens.MediumPadding1
import com.boiko.newsapp.presentation.details.components.DetailsTopBar
import com.boiko.newsapp.presentation.search.SearchEvent
import com.boiko.newsapp.ui.theme.NewsAppTheme

@Composable
fun DetailsScreen(
    article: Article,
    event: (DetailsEvent) -> Unit,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        DetailsTopBar(
            onBrowsingClick = {
                Intent(Intent.ACTION_VIEW).also {
                    it.data = Uri.parse(article.url)
                    if (it.resolveActivity(context.packageManager) != null) {
                        context.startActivity(it)
                    }
                }
            },
            onShareClick = {
                Intent(Intent.ACTION_SEND).also {
                    it.putExtra(Intent.EXTRA_TEXT, article.url)
                    it.type = "text/plain"
                    if (it.resolveActivity(context.packageManager) != null) {
                        context.startActivity(it)
                    }
                }
            },
            onBookmarkClick = { event(DetailsEvent.SaveArticle) },
            onBackClick = navigateUp
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = MediumPadding1,
                end = MediumPadding1,
                top = MediumPadding1
            )
        ) {
            item {
                AsyncImage(
                    model = ImageRequest.Builder(context = context).data(article.urlToImage).build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ArticleImageHeight)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.height(MediumPadding1))
                
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.displaySmall,
                    color = colorResource(id = R.color.text_title)
                )

                Text(
                    text = article.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(id = R.color.body)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailsScreenPreview() {
    NewsAppTheme(dynamicColor = false) {
        DetailsScreen(
            article = Article(
                author = "Annie Palmer",
                title = "Amazon ditches cashierless checkout system at its grocery stores - CNBC",
                description = "The company said it won't include the system, called Just Walk Out, in existing Fresh stores or new locations, and will instead rely more heavily on Dash Carts.",
                content = "Amazon is removing its cashierless checkout systems at Fresh supermarkets in the U.S., the company confirmed, marking the latest recalibration of its grocery strategy.\r\nThe company won't include the … [+2242 chars]",
                publishedAt = "2024-04-03T13:25:06Z",
                source = Source(
                    id = "",
                    name = "CNBC"
                ),
                url = "https://www.cnbc.com/2024/04/03/amazon-ditches-cashierless-checkout-system-at-its-grocery-stores.html",
                urlToImage = "\"https://image.cnbcfm.com/api/v1/image/107119370-1663261899454-gettyimages-1243254755-pas-l-hello-amazon-0916-dc-6.jpeg?v=1712148004&w=1920&h=1080"
            ),
            event = {}
        ) {
        }
    }
}